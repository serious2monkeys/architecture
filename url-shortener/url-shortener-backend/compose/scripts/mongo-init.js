db = db.getSiblingDB('admin')
db.auth(
    process.env.MONGODB_ROOT_USER,
    process.env.MONGODB_ROOT_PASSWORD)

db.createUser({
    user: process.env.MONGODB_USERNAME,
    pwd: process.env.MONGODB_PASSWORD,
    roles: [
        {
            role: 'readWrite',
            db: 'admin',
        },
        {
            role: 'readWrite',
            db: process.env.MONGODB_DATABASE,
        },
    ],
})

db = db.getSiblingDB(process.env.MONGODB_DATABASE)
db.auth(process.env.MONGODB_USERNAME, process.env.MONGODB_PASSWORD)
db.createCollection("pages")
db.pages.createIndex({"shortUrl": "hashed"}, {name: "page_short_url_idx"})
db.pages.createIndex({"longUrl": "hashed"}, {name: "page_long_url_idx"})
db.pages.createIndex({"shortUrl": 1, "longUrl": 1}, {name: "urls_index", unique: true})
db = db.getSiblingDB('admin')
db.auth(process.env.MONGODB_ROOT_USER, process.env.MONGODB_ROOT_PASSWORD)
sh.shardCollection(`${process.env.MONGODB_DATABASE}.pages`, {shortUrl: 1, longUrl: 1}, true)
