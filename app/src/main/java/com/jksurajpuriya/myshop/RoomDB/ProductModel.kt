package com.jksurajpuriya.myshop.RoomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NonNls

@Entity(tableName = "products")
data class ProductModel(

    @PrimaryKey
    @NonNls
    val productId : String,
    @ColumnInfo(name = "productName")
    val productName: String?="",
    @ColumnInfo(name = "productImage")
    val productImage: String? = "",
    @ColumnInfo(name = "productSp")
    val productSp: String?="",
)
