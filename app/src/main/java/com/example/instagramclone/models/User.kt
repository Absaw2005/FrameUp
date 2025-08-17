package com.example.instagramclone.models

class User {
     var name:String?=null
    var email:String?=null
     var image:String?=null
    var password:String?=null
    var uid:String?=null
    constructor()

    constructor(name: String?, email: String?, image: String?, password: String?) {
        this.name = name
        this.email = email
        this.image = image
        this.password = password
    }

    constructor(name: String?, email: String?, password: String?) {
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(name: String?, email: String?, image: String?, password: String?, uid: String?) {
        this.name = name
        this.email = email
        this.image = image
        this.password = password
        this.uid = uid
    }


}