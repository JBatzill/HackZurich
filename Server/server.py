import sys, hashlib, json, os, random

from azure.storage.blob import BlobService

from flask import Flask, request, redirect, url_for, send_file
from flask.ext.sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = './tmp/uploads/'

# azure sql (using FreeTDS)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mssql+pyodbc://azuresqluser:Awesome42@sqlserverdatasource'
db = SQLAlchemy(app)

# azure cloud storage
blob_service = BlobService(account_name='ggwpstorage', account_key='pwXjIuTp4q/Ar3mq2nvcww0/jmeNJtgzaxpu1EXqoBbchIC+EOyxCML79UXMdbO9Pc2L6IfCqLUiA/Jhs1MfJA==')
def save_file(file, filename):
    blob_service.put_block_blob_from_file('photos',filename,file,x_ms_blob_content_type='image/jpeg')
    return filename
def get_file(filename):
    path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
    blob_service.get_blob_to_path('photos', filename, path)
    return send_file(path, mimetype='image/jpeg')

# relational model
memberships = db.Table('memberships',
    db.Column('user_id', db.Integer, db.ForeignKey('user.id')),
    db.Column('group_id', db.Integer, db.ForeignKey('group.id'))
)

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    firstname = db.Column(db.String(120))
    lastname = db.Column(db.String(120))
    email = db.Column(db.String(120), unique=True)
    password = db.Column(db.String(120))
    groups = db.relationship('Group', backref='user', lazy='dynamic')
    
    def __init__(self, firstname, lastname, email, password):
        self.firstname = firstname
        self.lastname = lastname
        self.email = email
        self.password = password

    def __repr__(self):
        return '<User %r>' % self.email
        
class Group(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    creator_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    members = db.relationship('User', secondary=memberships, backref=db.backref('group', lazy='dynamic'))
    shares = db.relationship('Share', backref='group', lazy='dynamic')
    
    def __init__(self, creator_id):    
        self.creator_id = creator_id
        self.members.append(User.query.filter_by(id = creator_id).first())

    def __repr__(self):
        return '<Group %r>' % self.id
        
class Photo(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    creator_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    path = db.Column(db.String(255))
    
    def __init__(self, creator_id, path):
        self.creator_id = creator_id
        self.path = path

    def __repr__(self):
        return '<Photo %r>' % self.path
        
class Share(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    photo_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    group_id = db.Column(db.Integer, db.ForeignKey('group.id'))
    
    def __init__(self, photo_id, group_id):
        self.photo_id = photo_id
        self.group_id = group_id

    def __repr__(self):
        return '<Photo %r>' % self.path

class Session(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    session_key = db.Column(db.String(120), unique = True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    
    def __init__(self, user_id):
        self.user_id = user_id
        self.session_key = hashlib.md5("{0}".format(random.randint(0, 100000000))).hexdigest()

@app.route("/api/", methods = ['POST'])
def api():
    return "Hello"

@app.route("/api/register", methods = ['POST'])
def register():
    firstname = request.form['firstname']
    lastname = request.form['lastname']
    email = request.form['email']
    password = request.form['password']
    
    print "YO"
    
    if User.query.filter_by(email = email).first() is not None:
        return "Error: User name already in use."
    else:
        user = User(firstname, lastname, email, password)
        db.session.add(user)
        db.session.commit()
        return "{0}".format(User.query.filter_by(email = email).first().id)
    
    return "Error"
    
@app.route("/api/login", methods = ['POST'])
def login():
    email = request.form['email']
    password = request.form['password']
    
    print email, password
    
    if User.query.filter_by(email = email).first() is None:
        return "Error: User not found"
    else:
        user = User.query.filter_by(email = email).first()
        if user.password == password:
            session = Session(user_id = user.id)
            db.session.add(session)
            db.session.commit()
            return session.session_key
            
    return "Error"

@app.route("/api/<session_key>/group", methods = ['POST'])
def add_group(session_key):
    session = Session.query.filter_by(session_key = session_key).first()
    if session is not None:
        user = User.query.filter_by(id = session.user_id).first()
    else:
        return "Error: Not Logged In."
    
    group = Group(creator_id = user.id)
    db.session.add(group)
    db.session.commit()
    
    return "{0}".format(group.id)
    
@app.route("/api/<session_key>/group", methods = ['GET'])
def get_groups(session_key):
    session = Session.query.filter_by(session_key = session_key).first()
    if session is not None:
        user = User.query.filter_by(id = session.user_id).first()
    else:
        return "Error: Not Logged In."
    
    groups_serialized = json.dumps([group.id for group in user.groups])[1:-1]
    return groups_serialized

@app.route("/api/<session_key>/group/<int:id>/members", methods = ['POST'])
def group_members_add(session_key, id):
    session = Session.query.filter_by(session_key = session_key).first()
    if session is not None:
        user = User.query.filter_by(id = session.user_id).first()
    else:
        return "Error: Not Logged In."
        
    group = Group.query.filter_by(id = id).first()
    if group is not None:
        if user.id == group.creator_id or user in group.Members:
            email = request.form['email']
            user_to_add = User.query.filter_by(email = email).first()
            if user_to_add is not None:
                if user_to_add not in group.members:
                    group.members.append(user_to_add)
                    db.session.commit()
                return "Success"
            else:
                return "Error: User not found."
        else:
            return "Error: Access to group not granted."
    else:
        return "Error: Group not found."

@app.route("/api/<session_key>/group/<int:id>/members", methods = ['GET'])
def group_members_get(session_key, id):
    session = Session.query.filter_by(session_key = session_key).first()
    if session is not None:
        user = User.query.filter_by(id = session.user_id).first()
    else:
        return "Error: Not Logged In."
        
    group = Group.query.filter_by(id = id).first()
    if group is not None:
        if user.id == group.creator_id or user in group.Members:
            members_serialized = json.dumps([member.email for member in group.members])[1:-1]
            return members_serialized
        else:
            return "Error: Access to group not granted."
    else:
        return "Error: Group not found."
    
@app.route("/api/<session_key>/group/<int:id>/shares", methods = ['POST'])
def group_shares_add(session_key, id):
    session = Session.query.filter_by(session_key = session_key).first()
    if session is not None:
        user = User.query.filter_by(id = session.user_id).first()
    else:
        return "Error: Not Logged In."
        
    group = Group.query.filter_by(id = id).first()
    if group is not None:
        if user.id == group.creator_id or user in group.Members:
            photo_id = request.form['photo_id']
            photo_to_add = Photo.query.filter_by(id = photo_id).first()
            if photo_to_add is not None:
                share = Share(photo_id, group.id)
                if share not in group.shares:
                    group.shares.append(share)
                    db.session.add(share)
                    db.session.commit()
                return "Success"
            else:
                return "Error: Photo not found."
        else:
            return "Error: Access to group not granted."
    else:
        return "Error: Group not found."
    
@app.route("/api/<session_key>/group/<int:id>/shares", methods = ['GET'])
def group_shares_get(session_key, id):
    session = Session.query.filter_by(session_key = session_key).first()
    if session is not None:
        user = User.query.filter_by(id = session.user_id).first()
    else:
        return "Error: Not Logged In."
        
    group = Group.query.filter_by(id = id).first()
    if group is not None:
        if user.id == group.creator_id or user in group.Members:
            photos_serialized = json.dumps([share.photo_id for share in group.shares])[1:-1]
            return photos_serialized
        else:
            return "Error: Access to group not granted."
    else:
        return "Error: Group not found."

@app.route("/api/<session_key>/photo", methods = ['POST'])
def photo_add(session_key):
    session = Session.query.filter_by(session_key = session_key).first()
    if session is not None:
        user = User.query.filter_by(id = session.user_id).first()
    else:
        return "Error: Not Logged In."
        
    file = request.files['file']
    filename_md5 = hashlib.md5(file.read()).hexdigest() + os.path.splitext(file.filename)[1]
    file.seek(0)
    path = save_file(file, filename_md5)
    photo = Photo(creator_id = user.id, path = path)
    db.session.add(photo)
    db.session.commit()
    
    return "{0}".format(photo.id)
    
@app.route("/api/<session_key>/photo/<int:photo_id>", methods = ['GET'])
def photo_get(session_key, photo_id):
    session = Session.query.filter_by(session_key = session_key).first()
    if session is not None:
        user = User.query.filter_by(id = session.user_id).first()
    else:
        return "Error: Not Logged In."
    
    photo = Photo.query.filter_by(id = photo_id).first()
    
    if photo is not None:
        return get_file(photo.path)
    else:
        return "Error: Photo not found."

if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == 'create_db':
        db.create_all()
    elif len(sys.argv) > 1 and sys.argv[1] == 'create_storage':
        blob_service.create_container('photos')
        
    else:
        app.run(host = '0.0.0.0', port=5000, debug = True)