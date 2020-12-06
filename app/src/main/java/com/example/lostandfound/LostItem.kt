package com.example.lostandfound


// Todo - add or remove fields as needed
class LostItem {
    lateinit var name : String;
    lateinit var locationFound : String;
    lateinit var desc : String;
    lateinit var id: String;
    lateinit var uid: String;
    lateinit var dateFound: String;
    lateinit var datePosted: String;
    lateinit var imgURL : String;
    var status: Boolean

    constructor(uid: String, id: String, imgURL: String,name: String, locationFound: String,
                desc: String, dateFound: String, datePosted: String, status: Boolean) {
        this.uid = uid
        this.id = id
        this.name = name
        this.locationFound = locationFound
        this.desc = desc
        this.dateFound = dateFound
        this.datePosted = datePosted
        this.imgURL = imgURL
        this.status = status
    }

}
