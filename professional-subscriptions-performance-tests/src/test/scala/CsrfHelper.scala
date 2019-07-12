import io.gatling.core.Predef._
import io.gatling.core.check.CheckBuilder
import io.gatling.http.Predef.{Response, regex}
import io.gatling.http.check.HttpCheck

trait CsrfHelper {

  val CsrfPattern = """<input type="hidden" name="csrfToken" value="([^"]+)""""

  def saveCsrfToken: CheckBuilder[HttpCheck, Response, CharSequence, String] = {
    regex(_ => CsrfPattern).saveAs("csrfToken")
  }
}
