# order-manager-red
| Type Method  | Path Api | Content Type | Example |
| --- | --- | --- | --- |
| Get  | /user/logIn/{email}/{password} | email: String, password: String | "jane@example.com", "1234" |
| Post | /order/calculateOrderAmount| order: Order | ... |
| Get | /global/getCurrencies | none | ...|
| Put | /user/{userId} | userId: string | "123" |
|Delete | /user/{userId} | userId: string | "123" |
| Get | /user/getNamesOfCustomersByPrefix/{prefix} | prefix: string | "abc" |