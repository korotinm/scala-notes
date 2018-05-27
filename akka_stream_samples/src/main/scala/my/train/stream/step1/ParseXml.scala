package my.train.stream.step1

import java.util.concurrent.TimeUnit

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity.ChunkStreamPart
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ParseXml {

  implicit val system = ActorSystem("reactive-tweets")

  implicit val materializer = ActorMaterializer()

  implicit val execContext = scala.concurrent.ExecutionContext.global

  private val endTagOfArticle = "</ArticleItem>"
  private val extraLineRegexp = "<\\?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"\\?>|<IBTFxWire>|</IBTFxWire>"

  def main(args: Array[String]): Unit = {
    new Thread(() => WebServer.startServer("localhost", 8200)).start()


    val uri = Uri("http://localhost:8200/stream")

    val flow = Flow[ByteString]
      .filterNot(_.utf8String.matches(extraLineRegexp))
      .map(bs => ByteString(bs.utf8String.trim))
      .via(Framing.delimiter(ByteString(endTagOfArticle), maximumFrameLength = 10240))
      .map(_.utf8String)
      .map(_ + endTagOfArticle)
      .map(ByteString(_))

    Http().singleRequest(HttpRequest(uri = uri)) andThen {
      case Success(response) =>
        response.entity.dataBytes
          .via(flow)
          .map(_.utf8String).runForeach ( res =>
          //todo send res into actor
          println(res)
        )

      case Failure(ex) =>
        println(s"error: ${ex.getMessage}")
    }



    /* val finished = Http().singleRequest(HttpRequest(uri = uri)).flatMap { response =>
       response.entity.withSizeLimit(1024 * 1024 * 100).dataBytes
       .
     runForeach{ chunk =>
       println(chunk.utf8String + "--")
     }
     }*/

  }

}

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.HttpApp
import akka.http.scaladsl.server.Route

// Server definition
object WebServer extends HttpApp {
  val xml =
    """<?xml version="1.0" encoding="UTF-8"?>
 |<IBTFxWire>
 |    <ArticleItem id="1060088">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economic Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-EI</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:32</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[UK APRIL PUBLIC SECTOR NET DEBT EX-BANKS, EX-BOE TEMPORARY LENDING SCHEME 1.583 TRLN STG, EQUIVALENT TO 75.8 PCT OF GDP]]></Headline>
 |        <Headline2><![CDATA[Uk April Public Sector Net Debt Ex-banks, Ex-boe Temporary Lending Scheme 1.583 Trln Stg, Equivalent To 75.8 Pct Of Gdp]]></Headline2>
 |        <body><![CDATA[UK APRIL PUBLIC SECTOR NET DEBT EX-BANKS, EX-BOE TEMPORARY LENDING SCHEME 1.583 TRLN STG, EQUIVALENT TO 75.8 PCT OF GDP]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060087">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economy]]></NewsCategory>
 |        <NewsCategoryID>NC-EM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:31</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[UK ESTIMATE OF PUBLIC SECTOR NET DEBT EX-BANKS REDUCED BY 18.5 BLN STG (0.9 PCT OF GDP) AS OF END-MARCH 2018, REFLECTS PREVIOUS ERROR AND NEW DATA]]></Headline>
 |        <Headline2><![CDATA[Uk Estimate Of Public Sector Net Debt Ex-banks Reduced By 18.5 Bln Stg (0.9 Pct Of Gdp) As Of End-march 2018, Reflects Previous Error And New Data]]></Headline2>
 |        <body><![CDATA[UK ESTIMATE OF PUBLIC SECTOR NET DEBT EX-BANKS REDUCED BY 18.5 BLN STG (0.9 PCT OF GDP) AS OF END-MARCH 2018, REFLECTS PREVIOUS ERROR AND NEW DATA]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060086">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economic Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-EI</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:31</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[UK APRIL PUBLIC SECTOR NET DEBT EX-BANKS 1.777 TRLN STG, EQUIVALENT TO 85.1 PCT OF GDP]]></Headline>
 |        <Headline2><![CDATA[Uk April Public Sector Net Debt Ex-banks 1.777 Trln Stg, Equivalent To 85.1 Pct Of Gdp]]></Headline2>
 |        <body><![CDATA[UK APRIL PUBLIC SECTOR NET DEBT EX-BANKS 1.777 TRLN STG, EQUIVALENT TO 85.1 PCT OF GDP]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060085">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economic Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-EI</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:30</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[UK 2017/18 PUBLIC SECTOR NET BORROWING EX BANKS REVISED DOWN TO 40.487 BLN STG (2.0 PCT OF GDP) VS PREVIOUS ESTIMATE OF 42.635 BLN STG (2.1 PCT OF GDP)]]></Headline>
 |        <Headline2><![CDATA[Uk 2017/18 Public Sector Net Borrowing Ex Banks Revised Down To 40.487 Bln Stg (2.0 Pct Of Gdp) Vs Previous Estimate Of 42.635 Bln Stg (2.1 Pct Of Gdp)]]></Headline2>
 |        <body><![CDATA[UK 2017/18 PUBLIC SECTOR NET BORROWING EX BANKS REVISED DOWN TO 40.487 BLN STG (2.0 PCT OF GDP) VS PREVIOUS ESTIMATE OF 42.635 BLN STG (2.1 PCT OF GDP)]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060084">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[G20 Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-G20</NewsCategoryID>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:30</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[UNITED KINGDOM APR PSNCR, GBP DECREASE TO -9.71 BLN GB  VS PREV 1.31 BLN GB (REVISED FROM 0.473 BLN GB)]]></Headline>
 |        <Headline2><![CDATA[United Kingdom Apr Psncr, Gbp Decrease To -9.71 Bln Gb  Vs Prev 1.31 Bln Gb (revised From 0.473 Bln Gb)]]></Headline2>
 |        <body><![CDATA[UNITED KINGDOM APR PSNCR, GBP DECREASE TO -9.71 BLN GB  VS PREV 1.31 BLN GB (REVISED FROM 0.473 BLN GB)]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060083">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[G20 Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-G20</NewsCategoryID>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:30</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[UNITED KINGDOM APR PSNB, GBP INCREASE TO 6.23 BLN GB (FCAST 7.1 BLN GB) VS PREV -0.811 BLN GB (REVISED FROM -0.262 BLN GB)]]></Headline>
 |        <Headline2><![CDATA[United Kingdom Apr Psnb, Gbp Increase To 6.23 Bln Gb (fcast 7.1 Bln Gb) Vs Prev -0.811 Bln Gb (revised From -0.262 Bln Gb)]]></Headline2>
 |        <body><![CDATA[UNITED KINGDOM APR PSNB, GBP INCREASE TO 6.23 BLN GB (FCAST 7.1 BLN GB) VS PREV -0.811 BLN GB (REVISED FROM -0.262 BLN GB)]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060082">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[G20 Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-G20</NewsCategoryID>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:30</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[UNITED KINGDOM APR PSNB EX BANKS GBP INCREASE TO 7.84 BLN GB (FCAST 8.6 BLN GB) VS PREV 0.799 BLN GB (REVISED FROM 1.348 BLN GB)]]></Headline>
 |        <Headline2><![CDATA[United Kingdom Apr Psnb Ex Banks Gbp Increase To 7.84 Bln Gb (fcast 8.6 Bln Gb) Vs Prev 0.799 Bln Gb (revised From 1.348 Bln Gb)]]></Headline2>
 |        <body><![CDATA[UNITED KINGDOM APR PSNB EX BANKS GBP INCREASE TO 7.84 BLN GB (FCAST 8.6 BLN GB) VS PREV 0.799 BLN GB (REVISED FROM 1.348 BLN GB)]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060081">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:29</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS RISKS TO PRODUCTIVITY GROWTH OVER THE NEXT FEW YEARS ARE PROBABLY SKEWED TO THE DOWNSIDE OF BOE'S 1 PCT FORECAST]]></Headline>
 |        <Headline2><![CDATA[Bank Of England's Vlieghe Says Risks To Productivity Growth Over The Next Few Years Are Probably Skewed To The Downside Of Boe's 1 Pct Forecast]]></Headline2>
 |        <body><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS RISKS TO PRODUCTIVITY GROWTH OVER THE NEXT FEW YEARS ARE PROBABLY SKEWED TO THE DOWNSIDE OF BOE'S 1 PCT FORECAST]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060080">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:29</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[BOE'S VLIEGHE SAYS WEAK Q1 DID NOT TELL US VERY MUCH ABOUT THE LONG-TERM OUTLOOK, BUT ENOUGH SUCH THAT A MAY RATE RISE WAS NOT A GOOD IDEA]]></Headline>
 |        <Headline2><![CDATA[Boe's Vlieghe Says Weak Q1 Did Not Tell Us Very Much About The Long-term Outlook, But Enough Such That A May Rate Rise Was Not A Good Idea]]></Headline2>
 |        <body><![CDATA[BOE'S VLIEGHE SAYS WEAK Q1 DID NOT TELL US VERY MUCH ABOUT THE LONG-TERM OUTLOOK, BUT ENOUGH SUCH THAT A MAY RATE RISE WAS NOT A GOOD IDEA]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060079">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:28</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS LIKELY THAT THERE IS ONLY A SMALL AMOUNT OF SLACK LEFT IN UK ECONOMY, HE ESTIMATES BETWEEN ZERO AND HALF A PERCENT]]></Headline>
 |        <Headline2><![CDATA[Bank Of England's Vlieghe Says Likely That There Is Only A Small Amount Of Slack Left In Uk Economy, He Estimates Between Zero And Half A Percent]]></Headline2>
 |        <body><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS LIKELY THAT THERE IS ONLY A SMALL AMOUNT OF SLACK LEFT IN UK ECONOMY, HE ESTIMATES BETWEEN ZERO AND HALF A PERCENT]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060078">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:28</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS EVEN IF ADDITIONAL U.S. TRADE MEASURES DISCUSSED SO FAR ARE IMPLEMENTED, IT WOULD PROBABLY STILL NOT HAVE A MATERIAL EFFECT ON UK]]></Headline>
 |        <Headline2><![CDATA[Bank Of England's Vlieghe Says Even If Additional U.s. Trade Measures Discussed So Far Are Implemented, It Would Probably Still Not Have A Material Effect On Uk]]></Headline2>
 |        <body><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS EVEN IF ADDITIONAL U.S. TRADE MEASURES DISCUSSED SO FAR ARE IMPLEMENTED, IT WOULD PROBABLY STILL NOT HAVE A MATERIAL EFFECT ON UK]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060077">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:26</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS U.S. TRADE TARIFFS THAT HAVE BEEN IMPLEMENTED SO FAR HAVE BEEN TOO SMALL TO HAVE A MATERIAL EFFECT ON THE UK ECONOMY]]></Headline>
 |        <Headline2><![CDATA[Bank Of England's Vlieghe Says U.s. Trade Tariffs That Have Been Implemented So Far Have Been Too Small To Have A Material Effect On The Uk Economy]]></Headline2>
 |        <body><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS U.S. TRADE TARIFFS THAT HAVE BEEN IMPLEMENTED SO FAR HAVE BEEN TOO SMALL TO HAVE A MATERIAL EFFECT ON THE UK ECONOMY]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060076">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:25</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS UNWINDING OF ASSET PURCHASES IS LIKELY TO TAKE PLACE WHEN RATES HAVE MOVED UP SUFFICIENTLY THAT THEY CAN BE USED EFFECTIVELY IN EITHER DIRECTION]]></Headline>
 |        <Headline2><![CDATA[Bank Of England's Vlieghe Says Unwinding Of Asset Purchases Is Likely To Take Place When Rates Have Moved Up Sufficiently That They Can Be Used Effectively In Either Direction]]></Headline2>
 |        <body><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS UNWINDING OF ASSET PURCHASES IS LIKELY TO TAKE PLACE WHEN RATES HAVE MOVED UP SUFFICIENTLY THAT THEY CAN BE USED EFFECTIVELY IN EITHER DIRECTION]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060075">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:25</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[BOE'S VLIEGHE SAYS FIRMS FOR WHOM BREXIT IS IMPORTANT ARE INVESTING LESS THAN THOSE FOR WHOM IT IS NOT]]></Headline>
 |        <Headline2><![CDATA[Boe's Vlieghe Says Firms For Whom Brexit Is Important Are Investing Less Than Those For Whom It Is Not]]></Headline2>
 |        <body><![CDATA[BOE'S VLIEGHE SAYS FIRMS FOR WHOM BREXIT IS IMPORTANT ARE INVESTING LESS THAN THOSE FOR WHOM IT IS NOT]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060074">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:25</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS HIS CENTRAL FORECAST FOR POLICY RATES IS SLIGHTLY ABOVE CONDITIONING PATH OF RATES IN MAY INFLATION REPORT]]></Headline>
 |        <Headline2><![CDATA[Bank Of England's Vlieghe Says His Central Forecast For Policy Rates Is Slightly Above Conditioning Path Of Rates In May Inflation Report]]></Headline2>
 |        <body><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS HIS CENTRAL FORECAST FOR POLICY RATES IS SLIGHTLY ABOVE CONDITIONING PATH OF RATES IN MAY INFLATION REPORT]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060073">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:24</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS  HIS CENTRAL PROJECTION IS THAT ONE OR TWO QUARTER POINT RATE INCREASES WILL BE NEEDED PER YEAR OVER THREE-YEAR FORECAST PERIOD - WRITTEN ANSWERS TO LAWMAKERS]]></Headline>
 |        <Headline2><![CDATA[Bank Of England's Vlieghe Says  His Central Projection Is That One Or Two Quarter Point Rate Increases Will Be Needed Per Year Over Three-year Forecast Period - Written Answers To Lawmakers]]></Headline2>
 |        <body><![CDATA[BANK OF ENGLAND'S VLIEGHE SAYS  HIS CENTRAL PROJECTION IS THAT ONE OR TWO QUARTER POINT RATE INCREASES WILL BE NEEDED PER YEAR OVER THREE-YEAR FORECAST PERIOD - WRITTEN ANSWERS TO LAWMAKERS]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060072">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>CNY</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economy]]></NewsCategory>
 |        <NewsCategoryID>NC-EM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/CNY</FXPair>
 |        </CurrencyPairs>
 |        <Country>China</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:22</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[CHINA FINANCE MINISTRY SAYS AVERAGE IMPORT TARIFFS FOR QUALIFYING AUTO PARTS WILL BE 6 PCT AFTER NEW REDUCTION IS IMPLEMENTED]]></Headline>
 |        <Headline2><![CDATA[China Finance Ministry Says Average Import Tariffs For Qualifying Auto Parts Will Be 6 Pct After New Reduction Is Implemented]]></Headline2>
 |        <body><![CDATA[CHINA FINANCE MINISTRY SAYS AVERAGE IMPORT TARIFFS FOR QUALIFYING AUTO PARTS WILL BE 6 PCT AFTER NEW REDUCTION IS IMPLEMENTED]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060071">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>CNY</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economy]]></NewsCategory>
 |        <NewsCategoryID>NC-EM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/CNY</FXPair>
 |        </CurrencyPairs>
 |        <Country>China</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:22</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[CHINA FINANCE MINISTRY SAYS AVERAGE IMPORT TARIFF RATE FOR QUALIFYING VEHICLES WILL BE 13.8 PCT AFTER TARIFF CUT]]></Headline>
 |        <Headline2><![CDATA[China Finance Ministry Says Average Import Tariff Rate For Qualifying Vehicles Will Be 13.8 Pct After Tariff Cut]]></Headline2>
 |        <body><![CDATA[CHINA FINANCE MINISTRY SAYS AVERAGE IMPORT TARIFF RATE FOR QUALIFYING VEHICLES WILL BE 13.8 PCT AFTER TARIFF CUT]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060070">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>EUR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Money Market]]></NewsCategory>
 |        <NewsCategoryID>NC-MM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>EUR/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>Spain</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:21</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[SPANISH, PORTUGUESE BOND YIELDS TUMBLE 8-12 BPS ON DAY      , FALLING WITH ITALIAN PEERS]]></Headline>
 |        <Headline2><![CDATA[Spanish, Portuguese Bond Yields Tumble 8-12 Bps On Day      , Falling With Italian Peers]]></Headline2>
 |        <body><![CDATA[SPANISH, PORTUGUESE BOND YIELDS TUMBLE 8-12 BPS ON DAY      , FALLING WITH ITALIAN PEERS]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060069">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:20</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[BANK OF ENGLAND MPC'S VLIEGHE SAYS WE THINK INTEREST RATES WILL GO UP VERY GRADUALLY OVER THE NEXT FEW YEARS]]></Headline>
 |        <Headline2><![CDATA[Bank Of England Mpc's Vlieghe Says We Think Interest Rates Will Go Up Very Gradually Over The Next Few Years]]></Headline2>
 |        <body><![CDATA[BANK OF ENGLAND MPC'S VLIEGHE SAYS WE THINK INTEREST RATES WILL GO UP VERY GRADUALLY OVER THE NEXT FEW YEARS]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060068">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>EUR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Money Market]]></NewsCategory>
 |        <NewsCategoryID>NC-MM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>EUR/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>Italy</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:20</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[ITALY'S 10-YEAR BOND YIELD FALLS 3 BPS T0 2.31 PCT, DOWN FROM 14 MONTHS HIGH HIT IN EARLY TRADE]]></Headline>
 |        <Headline2><![CDATA[Italy's 10-year Bond Yield Falls 3 Bps T0 2.31 Pct, Down From 14 Months High Hit In Early Trade]]></Headline2>
 |        <body><![CDATA[ITALY'S 10-YEAR BOND YIELD FALLS 3 BPS T0 2.31 PCT, DOWN FROM 14 MONTHS HIGH HIT IN EARLY TRADE]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060067">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:19</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[BANK OF ENGLAND MPC'S VLIEGHE SAYS BREXIT UNCERTAINTY IS HAVING A DAMPENING EFFECT ON SOME OF THE ECONOMY]]></Headline>
 |        <Headline2><![CDATA[Bank Of England Mpc's Vlieghe Says Brexit Uncertainty Is Having A Dampening Effect On Some Of The Economy]]></Headline2>
 |        <body><![CDATA[BANK OF ENGLAND MPC'S VLIEGHE SAYS BREXIT UNCERTAINTY IS HAVING A DAMPENING EFFECT ON SOME OF THE ECONOMY]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060066">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>GBP</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>GBP/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>United Kingdom</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:19</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[BANK OF ENGLAND MPC'S VLIEGHE SAYS I DON'T THINK I HAVE A BIG DIFFERENCE WITH THE CENTRAL VIEW OF THE MPC]]></Headline>
 |        <Headline2><![CDATA[Bank Of England Mpc's Vlieghe Says I Don't Think I Have A Big Difference With The Central View Of The Mpc]]></Headline2>
 |        <body><![CDATA[BANK OF ENGLAND MPC'S VLIEGHE SAYS I DON'T THINK I HAVE A BIG DIFFERENCE WITH THE CENTRAL VIEW OF THE MPC]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060065">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>EUR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economy]]></NewsCategory>
 |        <NewsCategoryID>NC-EM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>EUR/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>Italy</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:18</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[SPAIN'S ECONOMY MINISTER SAYS ITALY HAS TO RESPECT RULES OF THE EUROPEAN UNION, SAYS WE COUNT ON ITAY TO ACT WITHIN THOSE RULES]]></Headline>
 |        <Headline2><![CDATA[Spain's Economy Minister Says Italy Has To Respect Rules Of The European Union, Says We Count On Itay To Act Within Those Rules]]></Headline2>
 |        <body><![CDATA[SPAIN'S ECONOMY MINISTER SAYS ITALY HAS TO RESPECT RULES OF THE EUROPEAN UNION, SAYS WE COUNT ON ITAY TO ACT WITHIN THOSE RULES]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060064">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>EUR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Money Market]]></NewsCategory>
 |        <NewsCategoryID>NC-MM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>EUR/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>Italy</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:17</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[ITALIAN/GERMAN 10-YEAR BOND YIELD SPREAD TIGHTENS TO 174 BPS      , FROM 189 BPS LATE MONDAY]]></Headline>
 |        <Headline2><![CDATA[Italian/german 10-year Bond Yield Spread Tightens To 174 Bps      , From 189 Bps Late Monday]]></Headline2>
 |        <body><![CDATA[ITALIAN/GERMAN 10-YEAR BOND YIELD SPREAD TIGHTENS TO 174 BPS      , FROM 189 BPS LATE MONDAY]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060063">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>IDR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Money Market]]></NewsCategory>
 |        <NewsCategoryID>NC-MM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/IDR</FXPair>
 |        </CurrencyPairs>
 |        <Country>Indonesia</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:17</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[INDONESIA BONDS MATURING IN MAY 2038  WEIGHTED AVG YIELD AT 7.99947 PCT- FIN MINISTRY OFFICIAL]]></Headline>
 |        <Headline2><![CDATA[Indonesia Bonds Maturing In May 2038  Weighted Avg Yield At 7.99947 Pct- Fin Ministry Official]]></Headline2>
 |        <body><![CDATA[INDONESIA BONDS MATURING IN MAY 2038  WEIGHTED AVG YIELD AT 7.99947 PCT- FIN MINISTRY OFFICIAL]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060062">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>IDR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Money Market]]></NewsCategory>
 |        <NewsCategoryID>NC-MM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/IDR</FXPair>
 |        </CurrencyPairs>
 |        <Country>Indonesia</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:17</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[INDONESIA BONDS MATURING IN MAY 2033  WEIGHTED AVG YIELD AT 7.91952 PCT- FIN MINISTRY OFFICIAL]]></Headline>
 |        <Headline2><![CDATA[Indonesia Bonds Maturing In May 2033  Weighted Avg Yield At 7.91952 Pct- Fin Ministry Official]]></Headline2>
 |        <body><![CDATA[INDONESIA BONDS MATURING IN MAY 2033  WEIGHTED AVG YIELD AT 7.91952 PCT- FIN MINISTRY OFFICIAL]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060061">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>IDR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Money Market]]></NewsCategory>
 |        <NewsCategoryID>NC-MM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/IDR</FXPair>
 |        </CurrencyPairs>
 |        <Country>Indonesia</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:16</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[INDONESIA BONDS MATURING IN MAY 2028  WEIGHTED AVG YIELD AT 7.46711 PCT- FIN MINISTRY OFFICIAL]]></Headline>
 |        <Headline2><![CDATA[Indonesia Bonds Maturing In May 2028  Weighted Avg Yield At 7.46711 Pct- Fin Ministry Official]]></Headline2>
 |        <body><![CDATA[INDONESIA BONDS MATURING IN MAY 2028  WEIGHTED AVG YIELD AT 7.46711 PCT- FIN MINISTRY OFFICIAL]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060060">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>IDR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Money Market]]></NewsCategory>
 |        <NewsCategoryID>NC-MM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/IDR</FXPair>
 |        </CurrencyPairs>
 |        <Country>Indonesia</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:16</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[INDONESIA T-BILLS MATURING IN FEBRUARY 2019 WEIGHTED AVG YIELD AT 5.86091 PCT- FIN MINISTRY OFFICIAL]]></Headline>
 |        <Headline2><![CDATA[Indonesia T-bills Maturing In February 2019 Weighted Avg Yield At 5.86091 Pct- Fin Ministry Official]]></Headline2>
 |        <body><![CDATA[INDONESIA T-BILLS MATURING IN FEBRUARY 2019 WEIGHTED AVG YIELD AT 5.86091 PCT- FIN MINISTRY OFFICIAL]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060059">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>IDR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Money Market]]></NewsCategory>
 |        <NewsCategoryID>NC-MM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/IDR</FXPair>
 |        </CurrencyPairs>
 |        <Country>Indonesia</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:16</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[INDONESIA T-BILLS MATURING IN AUGUST 2018 WEIGHTED AVG YIELD AT 5.19545 PCT- FIN MINISTRY OFFICIAL]]></Headline>
 |        <Headline2><![CDATA[Indonesia T-bills Maturing In August 2018 Weighted Avg Yield At 5.19545 Pct- Fin Ministry Official]]></Headline2>
 |        <body><![CDATA[INDONESIA T-BILLS MATURING IN AUGUST 2018 WEIGHTED AVG YIELD AT 5.19545 PCT- FIN MINISTRY OFFICIAL]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060058">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>IDR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Money Market]]></NewsCategory>
 |        <NewsCategoryID>NC-MM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/IDR</FXPair>
 |        </CurrencyPairs>
 |        <Country>Indonesia</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:16</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[INDONESIA RAISES 15 TRLN RPH FROM DEBT AUCTION - FIN MINISTRY OFFICIAL]]></Headline>
 |        <Headline2><![CDATA[Indonesia Raises 15 Trln Rph From Debt Auction - Fin Ministry Official]]></Headline2>
 |        <body><![CDATA[INDONESIA RAISES 15 TRLN RPH FROM DEBT AUCTION - FIN MINISTRY OFFICIAL]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060057">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>CNY</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economy]]></NewsCategory>
 |        <NewsCategoryID>NC-EM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/CNY</FXPair>
 |        </CurrencyPairs>
 |        <Country>China</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:11</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[CHINA FINANCE MINISTRY SAYS IMPORT TARIFF CUTS ON AUTOS, AUTO PARTS AIM TO FURTHER OPEN UP MARKET]]></Headline>
 |        <Headline2><![CDATA[China Finance Ministry Says Import Tariff Cuts On Autos, Auto Parts Aim To Further Open Up Market]]></Headline2>
 |        <body><![CDATA[CHINA FINANCE MINISTRY SAYS IMPORT TARIFF CUTS ON AUTOS, AUTO PARTS AIM TO FURTHER OPEN UP MARKET]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060056">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>CNY</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economy]]></NewsCategory>
 |        <NewsCategoryID>NC-EM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/CNY</FXPair>
 |        </CurrencyPairs>
 |        <Country>China</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:11</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[CHINA FINANCE MINISTRY SAYS TO CUT TARIFFS FOR SOME AUTO PARTS TO 6 PCT]]></Headline>
 |        <Headline2><![CDATA[China Finance Ministry Says To Cut Tariffs For Some Auto Parts To 6 Pct]]></Headline2>
 |        <body><![CDATA[CHINA FINANCE MINISTRY SAYS TO CUT TARIFFS FOR SOME AUTO PARTS TO 6 PCT]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060055">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>CNY</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economy]]></NewsCategory>
 |        <NewsCategoryID>NC-EM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/CNY</FXPair>
 |        </CurrencyPairs>
 |        <Country>China</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:11</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[CHINA FINANCE MINISTRY SAYS AUTO TARIFFS CUTS EFFECTIVE AS OF JULY 1, 2018]]></Headline>
 |        <Headline2><![CDATA[China Finance Ministry Says Auto Tariffs Cuts Effective As Of July 1, 2018]]></Headline2>
 |        <body><![CDATA[CHINA FINANCE MINISTRY SAYS AUTO TARIFFS CUTS EFFECTIVE AS OF JULY 1, 2018]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060054">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>CNY</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economy]]></NewsCategory>
 |        <NewsCategoryID>NC-EM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/CNY</FXPair>
 |        </CurrencyPairs>
 |        <Country>China</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:11</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[CHINA FINANCE MINISTRY SAYS TO CUT AUTO TARIFFS TO 15 PCT FOR SOME VEHICLES]]></Headline>
 |        <Headline2><![CDATA[China Finance Ministry Says To Cut Auto Tariffs To 15 Pct For Some Vehicles]]></Headline2>
 |        <body><![CDATA[CHINA FINANCE MINISTRY SAYS TO CUT AUTO TARIFFS TO 15 PCT FOR SOME VEHICLES]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060053">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>USD</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Commodities]]></NewsCategory>
 |        <NewsCategoryID>NC-CM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>others</FXPair>
 |        </CurrencyPairs>
 |        <Commodities>
 |            <CommodityID>COG</CommodityID>
 |        </Commodities>
 |        <Country>United States</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:10</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[U.S. CRUDE OIL RISES TO $72.69 A BARREL, HIGHEST SINCE NOVEMBER 2014]]></Headline>
 |        <Headline2><![CDATA[U.s. Crude Oil Rises To $72.69 A Barrel, Highest Since November 2014]]></Headline2>
 |        <body><![CDATA[U.S. CRUDE OIL RISES TO $72.69 A BARREL, HIGHEST SINCE NOVEMBER 2014]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060052">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>CNY</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economy]]></NewsCategory>
 |        <NewsCategoryID>NC-EM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/CNY</FXPair>
 |        </CurrencyPairs>
 |        <Country>China</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:10</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[CHINA FINANCE MINISTRY SAYS TO CUT AUTO TARIFFS]]></Headline>
 |        <Headline2><![CDATA[China Finance Ministry Says To Cut Auto Tariffs]]></Headline2>
 |        <body><![CDATA[CHINA FINANCE MINISTRY SAYS TO CUT AUTO TARIFFS]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060051">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>ZAR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economy]]></NewsCategory>
 |        <NewsCategoryID>NC-EM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/ZAR</FXPair>
 |        </CurrencyPairs>
 |        <Country>South Africa</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:09</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[S.AFRICA'S FINMIN NENE SAYS OUTLOOK FOR TAX REVENUES REMAINS UNCHANGED]]></Headline>
 |        <Headline2><![CDATA[S.africa's Finmin Nene Says Outlook For Tax Revenues Remains Unchanged]]></Headline2>
 |        <body><![CDATA[S.AFRICA'S FINMIN NENE SAYS OUTLOOK FOR TAX REVENUES REMAINS UNCHANGED]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060050">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>EUR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economic Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-EI</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>EUR/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>Spain</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:08</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[SPAIN TRADE DEFICIT -3.7 PCT TO MARCH Y/Y - ECON MINISTRY]]></Headline>
 |        <Headline2><![CDATA[Spain Trade Deficit -3.7 Pct To March Y/y - Econ Ministry]]></Headline2>
 |        <body><![CDATA[SPAIN TRADE DEFICIT -3.7 PCT TO MARCH Y/Y - ECON MINISTRY]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060049">
 |        <NewsType>Analysis</NewsType>
 |        <NewsTypeID>NT03</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>TRY</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Technicals]]></NewsCategory>
 |        <NewsCategoryID>NC-TC</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/TRY</FXPair>
 |        </CurrencyPairs>
 |        <Country>Turkey</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:08</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[FxWirePro: Turkish lira trades marginally higher against U.S. dollar, bias remains bearish]]></Headline>
 |        <Headline2><![CDATA[Fxwirepro: Turkish Lira Trades Marginally Higher Against U.s. Dollar, Bias Remains Bearish]]></Headline2>
 |        <body><![CDATA[<ul>
 |	<li>USD/TRY is currently trading at 4.5470 levels.<br>
 |	&nbsp;</li>
 |	<li>It made intraday high at 4.5891 and low at 4.5448 levels.<br>
 |	&nbsp;</li>
 |	<li>Intraday bias remains slightly bearish for the moment.<br>
 |	&nbsp;</li>
 |	<li>Key resistances are seen at 4.5726, 4.5985, 4.6220 and 4.6448 marks respectively.<br>
 |	&nbsp;</li>
 |	<li>On the other side, A sustained close below 4.5726 will drag the parity down towards key supports around 4.5220, 4.5001, 4.4875, 4.4427 and 4.40 marks respectively.<br>
 |	&nbsp;</li>
 |	<li>Important to note here that 20D, 30D and 55D EMA heads up and confirms the bullish trend in a daily chart<strong>. </strong>Current downside movement is short term trend correction only.</li>
 |</ul>
 |
 |<p><em><strong>We prefer to go short on USD/TRY around 4.5500, stop loss at 4.6000 and target of 4.4875.</strong></em><br>
 |<br>
 |<em>FxWirePro launches Absolute Return Managed Program. For more details, visit&nbsp;</em><a><em>http://www.fxwirepro.com/invest</em></a></p>
 |]]></body>
 |    <Image>http://www.fxwirepro.com/assets/uploads/2018052276f3e6dff13f16e31.png</Image>
 |        </ArticleItem>
 |    <ArticleItem id="1060048">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>TRY</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/TRY</FXPair>
 |        </CurrencyPairs>
 |        <Country>Turkey</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:01</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[TURKISH CENTRAL BANK SAYS INTEREST RATES IN FOREX DEPO AUCTION 13.5 PCT FOR LIRA, 1.75 PCT FOR USD]]></Headline>
 |        <Headline2><![CDATA[Turkish Central Bank Says Interest Rates In Forex Depo Auction 13.5 Pct For Lira, 1.75 Pct For Usd]]></Headline2>
 |        <body><![CDATA[TURKISH CENTRAL BANK SAYS INTEREST RATES IN FOREX DEPO AUCTION 13.5 PCT FOR LIRA, 1.75 PCT FOR USD]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060047">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>TRY</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Central Banks]]></NewsCategory>
 |        <NewsCategoryID>NC-CB</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/TRY</FXPair>
 |        </CurrencyPairs>
 |        <Country>Turkey</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:01</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[TURKISH CENTRAL BANK SAYS OPENS $1.5 BLN FOREX DEPO AUCTION]]></Headline>
 |        <Headline2><![CDATA[Turkish Central Bank Says Opens $1.5 Bln Forex Depo Auction]]></Headline2>
 |        <body><![CDATA[TURKISH CENTRAL BANK SAYS OPENS $1.5 BLN FOREX DEPO AUCTION]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060046">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>CHF</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economic Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-EI</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/CHF</FXPair>
 |        </CurrencyPairs>
 |        <Country>Switzerland</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:01</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[SWISS TOTAL SIGHT DEPOSITS AT 576.38 BLN SFR IN WEEK ENDING MAY 18 VERSUS 576.241 BLN SFR A WEEK EARLIER]]></Headline>
 |        <Headline2><![CDATA[Swiss Total Sight Deposits At 576.38 Bln Sfr In Week Ending May 18 Versus 576.241 Bln Sfr A Week Earlier]]></Headline2>
 |        <body><![CDATA[SWISS TOTAL SIGHT DEPOSITS AT 576.38 BLN SFR IN WEEK ENDING MAY 18 VERSUS 576.241 BLN SFR A WEEK EARLIER]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060045">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>CHF</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Economic Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-EI</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>USD/CHF</FXPair>
 |        </CurrencyPairs>
 |        <Country>Switzerland</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>08:00</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[SWISS SIGHT DEPOSITS OF DOMESTIC BANKS AT 470.379 BLN SFR IN WEEK ENDING MAY 18 VERSUS 472.752 BLN SFR A WEEK EARLIER]]></Headline>
 |        <Headline2><![CDATA[Swiss Sight Deposits Of Domestic Banks At 470.379 Bln Sfr In Week Ending May 18 Versus 472.752 Bln Sfr A Week Earlier]]></Headline2>
 |        <body><![CDATA[SWISS SIGHT DEPOSITS OF DOMESTIC BANKS AT 470.379 BLN SFR IN WEEK ENDING MAY 18 VERSUS 472.752 BLN SFR A WEEK EARLIER]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060044">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>TWD</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[G20 Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-G20</NewsCategoryID>
 |        <Country>Taiwan</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>07:58</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[TAIWAN APR JOBLESS RATE INCREASE TO 3.69 %  VS PREV 3.67 %]]></Headline>
 |        <Headline2><![CDATA[Taiwan Apr Jobless Rate Increase To 3.69 %  Vs Prev 3.67 %]]></Headline2>
 |        <body><![CDATA[TAIWAN APR JOBLESS RATE INCREASE TO 3.69 %  VS PREV 3.67 %]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060043">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>EUR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Money Market]]></NewsCategory>
 |        <NewsCategoryID>NC-MM</NewsCategoryID>
 |        <CurrencyPairs>
 |            <FXPair>EUR/USD</FXPair>
 |        </CurrencyPairs>
 |        <Country>Germany</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>07:40</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[GERMANY SETS 0 PCT COUPON ON NEW 2-YR SCHATZ TO BE SOLD ON WEDNESDAY - BUNDESBANK]]></Headline>
 |        <Headline2><![CDATA[Germany Sets 0 Pct Coupon On New 2-yr Schatz To Be Sold On Wednesday - Bundesbank]]></Headline2>
 |        <body><![CDATA[GERMANY SETS 0 PCT COUPON ON NEW 2-YR SCHATZ TO BE SOLD ON WEDNESDAY - BUNDESBANK]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060042">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>SEK</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[G20 Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-G20</NewsCategoryID>
 |        <Country>Sweden</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>07:30</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[SWEDEN APR TOTAL EMPLOYMENT INCREASE TO 5.043 MLN  VS PREV 5.038 MLN]]></Headline>
 |        <Headline2><![CDATA[Sweden Apr Total Employment Increase To 5.043 Mln  Vs Prev 5.038 Mln]]></Headline2>
 |        <body><![CDATA[SWEDEN APR TOTAL EMPLOYMENT INCREASE TO 5.043 MLN  VS PREV 5.038 MLN]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060041">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>SEK</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[G20 Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-G20</NewsCategoryID>
 |        <Country>Sweden</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>07:30</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[SWEDEN APR UNEMPLOYMENT RATE INCREASE TO 6.8 % (FCAST 6.6 %) VS PREV 6.5 %]]></Headline>
 |        <Headline2><![CDATA[Sweden Apr Unemployment Rate Increase To 6.8 % (fcast 6.6 %) Vs Prev 6.5 %]]></Headline2>
 |        <body><![CDATA[SWEDEN APR UNEMPLOYMENT RATE INCREASE TO 6.8 % (FCAST 6.6 %) VS PREV 6.5 %]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060040">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>SEK</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[G20 Indicator]]></NewsCategory>
 |        <NewsCategoryID>NC-G20</NewsCategoryID>
 |        <Country>Sweden</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>07:30</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[SWEDEN APR UNEMPLOYMENT RATE SA INCREASE TO 6.3 % (FCAST 6.2 %) VS PREV 6.2 %]]></Headline>
 |        <Headline2><![CDATA[Sweden Apr Unemployment Rate Sa Increase To 6.3 % (fcast 6.2 %) Vs Prev 6.2 %]]></Headline2>
 |        <body><![CDATA[SWEDEN APR UNEMPLOYMENT RATE SA INCREASE TO 6.3 % (FCAST 6.2 %) VS PREV 6.2 %]]></body>
 |    </ArticleItem>
 |    <ArticleItem id="1060039">
 |        <NewsType>Headline Only</NewsType>
 |        <NewsTypeID>NT01</NewsTypeID>
 |        <Currencies>
 |            <CurrencyID>EUR</CurrencyID>
 |        </Currencies>
 |        <NewsCategory><![CDATA[Money Market]]></NewsCategory>
 |        <NewsCategoryID>NC-MM</NewsCategoryID>
 |        <Country>Italy</Country>
 |        <Date>20180522</Date>
 |        <TimeGMT>07:24</TimeGMT>
 |        <Revision>0</Revision>
 |        <Headline><![CDATA[ITALIAN 5-YEAR CDS HIT HIGHEST IN 7 MONTHS AT 142 BASIS POINTS AMID UNEASE OVER POTENTIAL COALITION GOVERNMENT'S SPENDING PLANS-IHS MARKIT]]></Headline>
 |        <Headline2><![CDATA[Italian 5-year Cds Hit Highest In 7 Months At 142 Basis Points Amid Unease Over Potential Coalition Government's Spending Plans-ihs Markit]]></Headline2>
 |        <body><![CDATA[ITALIAN 5-YEAR CDS HIT HIGHEST IN 7 MONTHS AT 142 BASIS POINTS AMID UNEASE OVER POTENTIAL COALITION GOVERNMENT'S SPENDING PLANS-IHS MARKIT]]></body>
 |    </ArticleItem>
 |</IBTFxWire>
 |
    """.stripMargin

  override def routes: Route = {
    path("stream") {
      val xmlSource: Source[String, NotUsed] = Source(xml.split("\n").toList)
      get {
        complete(
          HttpEntity(ContentTypes.`text/xml(UTF-8)`, xmlSource.map(x => ByteString.fromString(x)))
        )
      }
    }
  }
}

