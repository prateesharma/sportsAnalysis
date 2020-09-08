
import java.util.*
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.collections.MutableList

class teamDict{
    var team1 = ""
    var team2 = ""
    var team1Spread = ""
    var team2Spread =""
    var team1Odds = ""
    var team2Odds = ""
    var team1OverUnder = ""
    var team2OverUnder = ""
}

fun getAttributes(games: teamDict){
    println(games.team1)
    println(games.team1Spread)
    println(games.team1Odds)
    println(games.team1OverUnder)

    println(games.team2)
    println(games.team2Spread)
    println(games.team2Odds)
    println(games.team2OverUnder)
}


fun waitUntilPageIsReady(driver: ChromeDriver) {
    val executor = driver as JavascriptExecutor
    WebDriverWait(driver, 1)
        .until { executor.executeScript("return document.readyState") == "complete" }
}


fun buildMap(homeTeams:List<String>,awayTeams:List<String>, spread: List<WebElement>, spreadOdds: List<WebElement>, homeOverUnder: List<String>, awayOverUnder: List<String>): MutableMap<String, teamDict> {
    //Create a Map that will store the games and the associated odds and betlines with it
    val teamOdds = mutableMapOf<String,teamDict>()
    homeTeams?.forEachIndexed { i, team ->
        var game1 = teamDict()
        game1?.team1 = team
        game1?.team2 = awayTeams[i]

        game1?.team1OverUnder = homeOverUnder[i]
        game1?.team2OverUnder = awayOverUnder[i]

        teamOdds.put("Game "+i,game1)

    }
    print(teamOdds)

    return teamOdds


}


fun combineOverUnder(overUnder: List<WebElement>, uoCurrentHandicap: List<WebElement>): MutableList<String> {
    //Creates a new list that combines the over/under with the right odds
    val combinedList= mutableListOf<String>()
    //Iterates through the two lists and appends the values together
    for ((num,i) in uoCurrentHandicap.withIndex()){
        combinedList.add(overUnder[num].text+i.text)
    }
    //returns the new mutablelist type string
    return(combinedList)

}

fun getNbaDataFanDuel(driver1: ChromeDriver): MutableMap<String, teamDict> {
    driver1.get("https://co.sportsbook.fanduel.com/sports/navigation/830.1/10107.3")

    //Creates a list of team names
    var teams = driver1.findElementsByClassName("name")
    //Empty list for home teams
    var homeTeams = mutableListOf<String>()
    //emtpy list for away team
    var awayTeams = mutableListOf<String>()
    //map of games

    var gameName = ""

    var homeTeamSpreads = mutableListOf<String>()
    val uoCurrentHandicap = driver1.findElementsByClassName("uo-currenthandicap")
    val overUnder = driver1.findElementsByClassName("had-value")



    val overUnderFinal = combineOverUnder(overUnder, uoCurrentHandicap)
    val homeOverUnder = mutableListOf<String>()
    val awayOverUnder = mutableListOf<String>()

    val spread = driver1.findElementsByClassName("currenthandicap")
    val teamNames = driver1.findElementsByClassName("name")
    val spreadOdds = driver1.findElementsByClassName("selectionprice")


    teams?.forEachIndexed { i, team ->
        if(i%2==0){
            homeTeams.add(team.text)
        }
        else if(i%2 !=0) {
            awayTeams.add(team.text)
        }

    }

    overUnderFinal?.forEachIndexed{i:Int, overUnder1->
        if (i%2==0){
            homeOverUnder.add(overUnder1)
        }else{
            awayOverUnder.add(overUnder1)
        }
    }

    var map1 = buildMap(homeTeams,awayTeams,spread,spreadOdds,homeOverUnder,awayOverUnder)
    return map1


}


fun main(){
    //Uses the chromedriver to get the data from fanduel
    val driver1 = ChromeDriver();


    //Create lists for each of the data fields that we want to store
    var finalAns = getNbaDataFanDuel(driver1)
    for (games in finalAns.values){
        getAttributes(games)
    }




    }
