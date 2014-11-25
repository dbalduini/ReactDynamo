ReactDynamo
===========

Implementation of a non blocking Amazon DynamoDB client in Scala

## Configuration

Add a credential properties  to your home.

**~/.aws/credentials**


```
[default]
aws_access_key_id = <Your Access Key ID>
aws_secret_access_key = <Your Secret Key>
```

## Getting started

```
import io.react2.reactdynamo._
import io.react2.reactdynamo.Implicits._
```

### Dynamo Client

```
val driver = new DynamoDriver
val client = driver.connection()
```

## Dynamo Object

A Dynamo Object represents a Table for a model.


Ex:

```
case class User(name: String, age: Int, genre: Option[Char])

implicit object UserDO extends DynamoObject[User] {

  import Implicits._

  val tableName = "User"
  val hashPK = ("name", AttributeType.String)
  val rangePK = None

  def toItem(t: User): Item = obj(
    key("name").value(t.name),
    key("age").value(t.age),
    key("genre").valueOpt(t.genre))

  def fromItem(item: Item): User = User(
    item("name").read[String],
    item("age").read[Int],
    item.get("genre").readOpt[Char])

}

```

## Supported Operations

All supported operations can be found in the dynamo client obtained from the *driver.connection()* call. 

Any operation require an implicit Dynamo Object in the context

```
implicit val userDO = UserDO
```

### Table Operations
- Create
- Delete
- ListTables

#### _Code Samples_

```
implicit val timeout: Timeout = Timeout(2 seconds)

val userTable = Table[User]

val futureOfCreate = client.createTable(table)
val futureOfDelete = client.deleteTable(userTable.name)

```


### Item Operations
- Get
- Put
- Update
- Delete

#### _Code Samples_

```
implicit val timeout: Timeout = Timeout(2 seconds)

val userId = java.util.UUID.randomUUID.toString
val user = User(userId, 1985, Some('M'))
val key = Map("name" -> write(userId))
```

```
val future: Future[Option[User]] = client.getItem[User](key)
```

```
val futureOfDelete = client.deleteItem[User](key)
```

```
val item = key("age").update(55, AttributeAction.PUT)
val f = client.updateItem[User](key, item)
```


### Search Operations
- Scan

#### _Code Samples_

```
val query = "age" > 20
val future: Future[List[User]] = client.scan[User](query)
```

```
val query = (("age" >= 18) ++ ("genre" === 'M'))
val futureOfUsers = client.scan[User](query)
```



### TODO
- [ ] SearchOps.query
- [ ] BatchOps
