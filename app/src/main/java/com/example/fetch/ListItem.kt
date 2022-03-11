package com.example.fetch

import com.google.gson.annotations.SerializedName

class ListItem {
    @SerializedName("id")
    var id = ""
    @SerializedName("listId")
    var listId = ""
    @SerializedName("name")
    var name = ""
}