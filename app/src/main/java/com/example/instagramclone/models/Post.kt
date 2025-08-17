package com.example.instagramclone.models

class Post {
    var postUrl:String?= null
    var caption:String?=null
    var name:String?=null
    var time:String?=null
    var like:Int?=null
    var profilePic:String?=null
    var uploaderUid:String?=null
    var postId:String?= null


    constructor()

    constructor(postUrl: String?, caption: String?) {
        this.postUrl = postUrl
        this.caption = caption
    }

    constructor(postUrl: String?, caption: String?, name: String?, time: String?) {
        this.postUrl = postUrl
        this.caption = caption
        this.name = name
        this.time = time
    }

    constructor(
        postUrl: String?,
        caption: String?,
        name: String?,
        time: String?,
        like: Int?,
        profilePic: String?,
        uploaderUid: String?,
        postId: String?
    ) {
        this.postUrl = postUrl
        this.caption = caption
        this.name = name
        this.time = time
        this.like = like
        this.profilePic = profilePic
        this.uploaderUid = uploaderUid
        this.postId = postId
    }


}