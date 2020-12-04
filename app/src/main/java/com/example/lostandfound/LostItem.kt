package com.example.lostandfound


// Todo - add or remove fields as needed
class LostItem {
    var img : Int = 0;
    lateinit var name : String;
    lateinit var locationFound : String;
    lateinit var desc : String;
    lateinit var id: String;
    lateinit var uid: String;
    lateinit var dateFound: String;
    lateinit var datePosted: String;

    constructor(uid: String, id: String, img: Int, name: String, locationFound: String,
                desc: String, dateFound: String, datePosted: String) {
        this.uid = uid
        this.id = id
        this.img = img
        this.name = name
        this.locationFound = locationFound
        this.desc = desc
        this.dateFound = dateFound
        this.datePosted = datePosted
    }

}
