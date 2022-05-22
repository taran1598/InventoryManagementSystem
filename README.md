# InventoryManagementSystem

**To run in REPL**
- Create a REPL and choose import from github option
- Choose this repository
- REPL will create a new REPL
- In my versoin REPL asked for a run configuration. For running the project using the RUN button repl provides enter **"./gradlew bootRun"** .
  - If REPL did not ask for a run configuration, in console at the bottom enter **"./gradlew bootRun"**.
  **Thats all the steps to run the application in REPL**.
  
**Problems with running it**
- One problem with running "./gradlew" in REPL is that the permission may not be set. I have set the permission for this repo but just in case you get the error
**gradlew permission denied** , enter this command in the terminal where you ran **"./gradlew bootRun"**. Command: **chmod +x gradlew**. From the post https://stackoverflow.com/questions/17668265/gradlew-permission-denied .

### Workflows
- Mainpage has two options - Items and Warehouses
- To add an item. Click items -> add item at the top -> fill in form -> click submit
- To add a warehouse. Click warehouses -> add warehouse at the top -> fill in form -> click submit
- To add item to warehouse. Click items -> add item to warehouse button beside the item we want to add -> select the warehouse we want to add the item to by selecting choose -> fill in quantity to add to that warehouse
- To remove an item from the warehouse. Click warehouse -> view items in warehouse -> select Reduce Items on the item we want to reduce -> enter quantity to reduce
- To delete a warehouse or item, navigate to the item page to delete an item and warehouse page to delete the warehouse
- To edit item, navigate to the item page and click edit beside the item we want to edit -> fill in the form
- To edit a warehouse, navigate to the warehouse page, and click edit beside the warehouse we want to edit -> fill in the form
