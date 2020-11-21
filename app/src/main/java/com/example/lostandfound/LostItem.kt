// VIKRAM CODE STARTS HERE

package com.example.lostandfound


// Todo - add or remove fields as needed
class LostItem {
    var img : Int = 0;
    lateinit var name : String;
    lateinit var locationFound : String;
    lateinit var desc : String;

    constructor(img: Int, name: String, locationFound: String, desc: String) {
        this.img = img
        this.name = name
        this.locationFound = locationFound
        this.desc = desc
    }

}

// VIKRAM CODE ENDS HERE