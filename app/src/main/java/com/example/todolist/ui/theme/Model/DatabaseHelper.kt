import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class DatabaseHelper(private val context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        val query = "CREATE TABLE " + TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER , " +
                COLUMN_JOB + " TEXT, " +
                COLUMN_IS_DONE + " Bit )"
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addJob(id: Int,job: String?, is_done: Int?) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_ID, id)
        cv.put(COLUMN_JOB, job)
        cv.put(COLUMN_IS_DONE, is_done)
        val result = db.insert(TABLE_NAME, null, cv)
        if (result == -1L) {
            Toast.makeText(context, "Yapılacak listesine ekleyemedik", Toast.LENGTH_SHORT).show()
        }
    }

    fun readAllData(): Cursor? {
        val query = "SELECT * FROM " + TABLE_NAME
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun update_is_done(id : Int,is_done : Int){

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_IS_DONE,is_done)
        var str_id = id.toString()
        db.update(TABLE_NAME,cv,"id=?", arrayOf(str_id))
    }


    fun deleteAllJob() {

        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, null, null).toLong()
        if (result == -1L) {
            Toast.makeText(context, "Yapılacak Listesi Silinemedi", Toast.LENGTH_SHORT).show()
        }
    }


    fun deleteSelectedItem(id: String) {
        /*
        println("gelen id = $id")
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME2, "id=?", arrayOf(id)).toLong()
        if (result == -1L) {
            // başarısız
            Toast.makeText(context, "Ürün silme başarısız", Toast.LENGTH_SHORT).show()
        } else {
            // başarılı
            Toast.makeText(context, "Seçilen ürün silindi", Toast.LENGTH_SHORT).show()
        }

         */
    }

    companion object {
        private const val DATABASE_NAME = "jobs.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "job_list"
        private const val COLUMN_ID = "id"
        private const val COLUMN_JOB = "job"
        private const val COLUMN_IS_DONE = "is_done"

    }
}