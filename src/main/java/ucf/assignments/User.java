package ucf.assignments;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Andrew Shepard
 */
public class User {
    ObservableList<Item> todolist = FXCollections.observableArrayList();
    private String filePath = "resources/Example.json";
    int active_item_index = 0;
    // *** Constructor ***

    public ObservableList<Item> getTodolist() { return todolist; }

    public int findItem_index(Item item) {
        int item_index = todolist.indexOf(item);
        return item_index;
    }

    public int getActive_item_index() {
        return active_item_index;
    }

    public void setActive_item_index(int active_item_index){
        this.active_item_index = active_item_index;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setTodolist(ObservableList<Item> todolist) {
        this.todolist = todolist;
    }

    public List<Item> loadItems(){
        try {
            //open json
            File input = new File(this.filePath);
            JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
            JsonObject fileObject = fileElement.getAsJsonObject();
            JsonArray jsonArrayofItems = fileObject.get("Todolist").getAsJsonArray();
            List<Item> items = new ArrayList<>();
            //for each item in the json
            for(JsonElement itemElement : jsonArrayofItems){
                JsonObject itemJsonObject = itemElement.getAsJsonObject();

                String description = itemJsonObject.get("description").getAsString();
                Boolean completion_status = itemJsonObject.get("completion_status").getAsBoolean();
                LocalDate due_date = LocalDate.parse(itemJsonObject.get("due_date").getAsString());
                //add each object to lists
                Item item = new Item(description, completion_status, due_date);
                items.add(item);
            }
            return items;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Item getActiveList(int index){
        return todolist.get(index);

    }
    public void clearTodoList(){
        todolist.clear();
    }
    public void saveTodoLists(){
        //for each item in tl
        Gson gson = new Gson();
        List<ItemSaveIsScuffed> isisList = new ArrayList<>();
        for (Item item : todolist){
            ItemSaveIsScuffed isis_item =  new ItemSaveIsScuffed(
                    (String) item.getDescription(),
                    item.getCompletion_status(),
                    item.getDue_date().toString()
            );
            isisList.add(isis_item);
        }
        try {
            //add the items to the json
            FileWriter fw = new FileWriter(filePath);
            fw.write("{\"Todolist\":"+gson.toJson(isisList)+"}");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addItem(String description, Boolean completion_status, LocalDate due_date){
        //lists add new item with description, completion and due date
        Item added_item = new Item(description,completion_status,due_date);
        todolist.add(added_item);   
    }
    public void removeItem(Item item){
        //tl remove the indexed item from the list
        todolist.remove(item);
    }
    public void editItemDescription(Item i, String description){
        //i will be referenced from lists
        //i set item description to the string using item.setdescription
        i.setDescription(description);
    }
    public void editItemDueDate(Item i, LocalDate due_date) {
        //i will be referenced from lists
        //set i's description using item's setdescription
        i.setDue_date(due_date);
    }
    public void editItemCompetionStatus(Item i,Boolean status){
        //i will be referenced from lists
        //use item's set completionstatus to move the item to true
        i.setCompletion_status(status);
    }
    public ObservableList<Item> getCompleteItems(){
        //create a temporary list
        ObservableList<Item> complete_items = FXCollections.observableArrayList();
        //for every item in tl
        for(Item item : todolist){
            // check if the item has true for completion status
            if(item.completion_status == true){
                //if it does add it to the temporary list
                complete_items.add(item);
            }
        }
        return complete_items;
    }
    public ObservableList<Item> getIncompleteItems(){
        //create a temporary list
        ObservableList<Item> complete_items = FXCollections.observableArrayList();
        //for every item in tl
        for(Item item : todolist){
            // check if the item has false for completion status
            if(item.completion_status == false){
                //if it does add it to the temporary list
                complete_items.add(item);
            }
        }
        return complete_items;

    }
    public static Comparator<Item> dateComparator = new Comparator<Item>() {
        public int compare(Item item_1, Item item_2) {
            LocalDate  item_1Date = item_1.getDue_date();
            LocalDate item_2Date = item_2.getDue_date();
            //ascending order
            return item_1Date.compareTo(item_2Date);
        }};
    public void sortItemByDate(){
        Collections.sort(todolist,dateComparator);
    }
    public ObservableList<Item> getAllItems(){
        return todolist;
    }
}
