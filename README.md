# order-manager-red

| Type Method | Path Api                                                                                               | Content Type                                                                                             | Example                                                |
|-------------|--------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|--------------------------------------------------------|
| Get         | /user/logIn/{email}/{password}                                                                         | email: String, password: String                                                                          | "jane@example.com", "1234"                             |
| Post        | /order/calculateOrderAmount                                                                            | order: Order                                                                                             | ...                                                    |
| Get         | /global/getCurrencies                                                                                  | none                                                                                                     | ...                                                    |
| Put         | /user/{userId}                                                                                         | userId: string                                                                                           | "123"                                                  |
| Delete      | /user/{userId}                                                                                         | userId: string                                                                                           | "123"                                                  |
| Get         | /user/getNamesOfCustomersByPrefix/{prefix}                                                             | prefix: string                                                                                           | "abc"                                                  |
| Post        | /user/signUp?fullName=TOm& companyName=Poto& email=Poto@gmail.com& currency=SHEKEL& password=123456789 | fullName:string, companyName:string, currency:Currency, email:string, currency:Currency, password:string | "TOm", "Poto", "Poto@gmail.com", "SHEKEL", "123456789" |
| Get         | /graph/{rangeOfMonths}                                                                                 | rangeOfMonths:int                                                                                        | 3                                                      |

## Docker Compose for Services: mongodb, redis and rabbitmq

### Prerequisites

Before using these scripts, you need to install Docker by following these steps:

1. Go to https://docs.docker.com/desktop/install/windows-install/
2. Follow the section: Install interactively🔗
3. After the installation, verify Docker by running the command: `docker version`
4. Stop the mongodb you already installed!!!!

#### Start/Stop services

1. Go to terminal and open a Command Prompt or PowerShell window
2. Execute the `start.bat` script (in first time it may take time)
    * Launch the applications:
        * Monogdb:     http://localhost:8081/
        * Redis:       http://localhost:8082/
        * Rabbitmq:    http://localhost:15672/ username/pass: admin/admin
3. Execute the `stop.bat` for stop all services

