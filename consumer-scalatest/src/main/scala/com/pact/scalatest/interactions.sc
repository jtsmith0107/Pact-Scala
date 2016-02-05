import au.com.dius.pact.model.Request

//method: GET
//path: /path/to/cool
//  query: [:]
//headers: [Connection:keep-alive, Content-Length:4, Accept:*/*, Content-Type:text/plain; charset=UTF-8, User-Agent:Dispatch/0.11.3, Host:localhost:29829]
//	matchers: null
//	body: nice



val expected = new Request {
  val method = "GET"
  val path = "path/to/cool"
}
expected
("GET", , Map.empty, Map("Connection" -> "keep-alive"), "nice", null)


val actual = new Request("GET", "path/to/cool", Map.empty, Map("Connection" -> "keep-alive"), "nice", null)


//method: GET
//path: /path/to/cool
//  query: [:]
//headers: [Connection:keep-alive, Content-Length:4, Accept:*/*, Content-Type:text/plain; charset=UTF-8, User-Agent:Dispatch/0.11.3, Host:localhost:37074]
//	matchers: null
//	body: nice

//	method: GET
//	path: /path/to/cool
//	query: [:]
//headers: [Connection:keep-alive, Content-Length:4, Accept:*/*, Content-Type:text/plain; charset=UTF-8, User-Agent:Dispatch/0.11.3, Host:localhost:37074]
//matchers: null
//body: nice
//
//
//	PactSessionResults(List(),List(),List(),List(	method: GET
//	path: /path/to/cool
//	query: [:]
//	headers: [Connection:keep-alive, Content-Length:4, Accept:*/*, Content-Type:text/plain; charset=UTF-8, User-Agent:Dispatch/0.11.3, Host:localhost:29829]
//matchers: null
//body: nice, 	method: GET
//path: /path/to/cool
//  query: [:]
//headers: [Connection:keep-alive, Content-Length:4, Accept:*/*, Content-Type:text/plain; charset=UTF-8, User-Agent:Dispatch/0.11.3, Host:localhost:29829]
//	matchers: null
//	body: nice, 	method: GET
//	path: /path/to/cool
//	query: [:]
//	headers: [Connection:keep-alive, Content-Length:4, Accept:*/*, Content-Type:text/plain; charset=UTF-8, User-Agent:Dispatch/0.11.3, Host:localhost:29829]
//matchers: null
//body: nice))