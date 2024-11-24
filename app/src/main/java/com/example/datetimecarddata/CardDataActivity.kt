package com.example.datetimecarddata

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

class CardDataActivity : AppCompatActivity() {

    private lateinit var toolbarMain: Toolbar
    private lateinit var nameTV: TextView
    private lateinit var surNameTV: TextView
    private lateinit var phoneTV: TextView
    private lateinit var ageTV: TextView
    private lateinit var imageIV: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_card_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbarMain = findViewById(R.id.toolbarMain)
        nameTV = findViewById(R.id.nameTV)
        surNameTV = findViewById(R.id.surNameTV)
        phoneTV = findViewById(R.id.phoneTV)
        ageTV = findViewById(R.id.ageTV)
        imageIV = findViewById(R.id.imageIV)

        setSupportActionBar(toolbarMain)
        title = "Карточка данных"
        toolbarMain.subtitle = "by Rocky"

        val person: Person = intent.extras?.getSerializable("person") as Person

        val name = person.name
        val surName = person.surName
        val phone = person.phone
        val birthday = person.birthday
        val image: Uri? = Uri.parse(person.image)


        val list = ageAndDaysBeforeBirthday(birthday!!)

        nameTV.text = name
        surNameTV.text = surName
        phoneTV.text = phone
        if (list[2] == "некорректно") {
            ageTV.text = """
                ${list[0]}
                ${list[1]} ${list[2]}
            """.trimIndent()
        }
        else {
            ageTV.text = """
            ${list[0]} лет
            До дня рождения:
            ${list[1]} месяцев
            ${list[2]} дней
        """.trimIndent()
        }

        if (person.image != "null") imageIV.setImageURI(image)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain -> finishAffinity()
        }
        return super.onOptionsItemSelected(item)
    }

    fun ageAndDaysBeforeBirthday(birthday: String): List<String> {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val dateBirthday = try {
            LocalDate.parse(birthday, formatter)
        } catch (e: DateTimeParseException) {
            return listOf("Данные дня рождения", "введены", "некорректно")
        }

        val dateNow = LocalDate.now()
        val yearNow = dateNow.year
        val yearBirthday = dateBirthday.year

        val personAgeNow = ChronoUnit.YEARS.between(dateBirthday, dateNow)
        val theDifferenceInYears = yearNow - yearBirthday
        var months = 0L
        var days = 0L

        var dateBirthdayNowYear = LocalDate.now()
        if (personAgeNow < theDifferenceInYears) {
            dateBirthdayNowYear = dateBirthday.plusYears(theDifferenceInYears.toLong())
        } else {
            dateBirthdayNowYear = dateBirthday.plusYears(personAgeNow)
        }
        val monthsBeforeBirthday = ChronoUnit.MONTHS.between(dateNow, dateBirthdayNowYear)
        var birthdayNextYear = LocalDate.now()
        if (monthsBeforeBirthday < 0) {
            birthdayNextYear = dateBirthdayNowYear.plusYears(1)
            months = ChronoUnit.MONTHS.between(dateNow, birthdayNextYear)
            val dayBeforeBirthday = ChronoUnit.DAYS.between(dateNow, birthdayNextYear)
            val oneMonthBeforeBirthday = birthdayNextYear.minusMonths(1)
            val daysBeforeoneMonthBeforeBirthday =
                ChronoUnit.DAYS.between(dateNow, oneMonthBeforeBirthday)
            days = dayBeforeBirthday - daysBeforeoneMonthBeforeBirthday
        } else {
            if (monthsBeforeBirthday != 0L) {
                months = monthsBeforeBirthday
                val dayBeforeBirthday = ChronoUnit.DAYS.between(dateNow, dateBirthdayNowYear)
                val oneMonthBeforeBirthday = dateBirthdayNowYear.minusMonths(1)
                val daysBeforeoneMonthBeforeBirthday =
                    ChronoUnit.DAYS.between(dateNow, oneMonthBeforeBirthday)
                days = dayBeforeBirthday - daysBeforeoneMonthBeforeBirthday
            } else {
                months = 0
                days = ChronoUnit.DAYS.between(dateNow, dateBirthdayNowYear)
                if (days < 0) {
                    days = ChronoUnit.DAYS.between(dateNow, dateBirthdayNowYear.plusYears(1))
                }
            }
        }
        val list = listOf(personAgeNow.toString(), months.toString(), days.toString())
        return list
    }

}