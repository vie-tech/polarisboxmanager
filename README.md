Setup & Run

# Clone
git clone https://github.com/vie-tech/polarisboxmanager.git

# Build
mvn clean package -DskipTests

# Run
docker-compose up --build

# Stop
docker-compose down

# Test Data (Already in DB)

user: {
"_id": "68db16228fc6e5fc606054b0",
"userPublicId": "4352e02d-bfda-42ce-ba5b-d2eee6d9616b",
"username": "favour",
"email": "favour@mail.com",
"tripsCompleted": 0,
"isActive": false,
"_class": "com.ativie.userservice.model.UserAccount"
}

item: {
"_id": "68dbd9eb0764be00194b68f6",
"code": "ITEM_707770",
"itemPublicId": "ec72c13f-e6a6-4584-8144-d156025deeb4",
"name": "mango",
"weight": 155.6,
"_class": "com.ativie.itemservice.model.Item"
}

box: {
"_id": "68dc49d1f6ba941910baf5cf",
"boxPublicId": "e7e2de3b-ab76-4819-982f-5d88272f26af",
"name": "blackbox",
"weightLimit": 500,
"batteryCapacity": 100,
"userPublicId": "4352e02d-bfda-42ce-ba5b-d2eee6d9616b",
"currentWeightCapacity": 0,
"state": "IDLE",
"txref": "89807d10-5841-4597-8dbe-943356ff169b",
"charging": false,
"createdAt": "2025-09-30T21:21:21.876+00:00",
"lastModifiedAt": "2025-09-30T21:21:21.876+00:00",
"_class": "com.ativie.boxservice.model.Box"
}

# Create Box

POST http://localhost:8080/api/box/create
{
"name": "whitebox",
"userPublicId": "4352e02d-bfda-42ce-ba5b-d2eee6d9616b"
}

# Add Item to Box

POST http://localhost:8080/api/box/add
{
"itemName": "mango",
"itemWeight": 100.0,
"boxTxref": "89807d10-5841-4597-8dbe-943356ff169b",
"userPublicId": "4352e02d-bfda-42ce-ba5b-d2eee6d9616b",
"itemCode": "ITEM_707770"
}

# Create Item

POST http://localhost:8080/api/item/create
{
"name": "apple",
"weight": 155.6
}

# Get Items in Box

GET http://localhost:8080/api/box/checkLoadedItems/{boxPublicId}

# Get Idle Boxes

GET http://localhost:8082/api/box/getIdle

# Get Box Battery (pass txref)

GET http://localhost:8080/api/box/getBattery/{txref}

**System Requirements:**  
Docker must be installed on the host system prior to running this application.

**Database Configuration:**  
MongoDB connection strings are intentionally hardcoded in the configuration files to facilitate straightforward setup
and review of the application. In a production environment, these credentials would be externalized using environment
variables or secure configuration management tools. A test database instance has been provided for evaluation purposes.