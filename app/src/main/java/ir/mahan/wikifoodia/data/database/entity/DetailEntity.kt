package ir.mahan.wikifoodia.data.database.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.mahan.wikifoodia.models.detail.ResponseDetail
import ir.mahan.wikifoodia.utils.constants.Constants

@Entity(tableName = Constants.DETAIL_TABLE_NAME)
data class DetailEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val result: ResponseDetail
)
