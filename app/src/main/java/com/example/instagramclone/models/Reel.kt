package com.example.instagramclone.models

class Reel {
    var videoUrl:String?= null
    var caption:String?=null
    var profileLink:String?=null
    var profileName:String?=null
    var uploaderUid:String?=null
    var uid:String?=null
    var like:Int?= null
    var time:String?=null

    constructor()

    constructor(videoUrl: String?, caption: String?) {
        this.videoUrl = videoUrl
        this.caption = caption
    }

    constructor(videoUrl: String?, caption: String?, profileLink: String?) {
        this.videoUrl = videoUrl
        this.caption = caption
        this.profileLink = profileLink
    }

    constructor(videoUrl: String?, caption: String?, profileLink: String?, profileName: String?) {
        this.videoUrl = videoUrl
        this.caption = caption
        this.profileLink = profileLink
        this.profileName = profileName
    }

    constructor(
        videoUrl: String?,
        caption: String?,
        profileLink: String?,
        profileName: String?,
        uploaderUid: String?,
        uid: String?,
        like: Int?,
        time: String?
    ) {
        this.videoUrl = videoUrl
        this.caption = caption
        this.profileLink = profileLink
        this.profileName = profileName
        this.uploaderUid = uploaderUid
        this.uid = uid
        this.like = like
        this.time = time
    }


}