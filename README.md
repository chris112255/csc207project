# CSC207 Project
User Stories: 

The user wants a page where they can easily go to their saved recipes or a page where they can add recipes to their saved recipes. [Team Story]

Use Case: By running the program, a jframe is opened where there are three buttons: "Favourites", "Search Recipes" and "Meal Planner".

User Story: The user wants to find recipes that match their dietary preferences and ingredient requirements so they can discover new meals to cook. [Huzaifah's Story]
Use Case: When the user enters ingredients in the search field and selects dietary filters like protein requirements or calorie limits, the system queries the Edamam API with these parameters. The system then displays matching recipes in the panel. If no recipes match the criteria, the system shows a "No recipes found" message and suggests adjusting the search parameters.

User Story: The user wants to filter recipe results using specific nutritional constraints and dietary restrictions so they can find recipes that align with their health goals. [Huzaifah's Story]
Use Case: The user goes on the search page and enters specific nutritional limits such as minimum protein grams, maximum fat grams, maximum sugar grams, and maximum carbohydrates. When the user clicks search, the system combines all these filters into a single API request to Edamam and returns only recipes that meet all specified criteria. If the filters are too restrictive and return no results, the system displays a message suggesting the user adjust their criteria for better results.


The user wantes to save recipes from the search page and they want to be able to remove them as needed. [Huzaifah's Story]
Use Case: Clicking the "save" button under a recipe checks if a recipe is already in favourites; If it isn't it saves it, if it is, it displays a message saying it is. If the recipe is saved, the button changes to "Remove from Saved" and there is a popup confirming the save. The saved recipe window is automatically refreshed when this happens, and the recipe now appears under saved recipe view. In this new page, the user can remove the recipe from favourites, and the button in the search page is updated to "Save" instead of "remove from favourites".

Use Case: The "Search Recipes" button opens a new jframe that allows the user to search for recipes based on their needs. For example they can search the main ingredient, or search recipes based on their macro requirements.

Use Case: The "Meal Planner" button opens a new jframe that allows the user to set macro goals and see how if their planned meals reach the goals or not and by how much. 

The user has saved some of their favorite recipes in the past but is now on a diet. They will be able to filter their recipes according to their macro requirements, allergies, type of diet, the number of ingredients, and the main ingredient in the recipe. The user should also be able to delete recipes from their favorites. [Team Story] 

Use Case: On the "Favourites" Jframe, there are several text fields which allow the user to filter.

 
User Story: For sugar: The user clicks on the recipe he wants to create. It opens a new jframe specifying details of the recipe, and if there is more than 15% daily value of a macro, it will display a warning specifying it on the recipes jframe.

Diddy has a daily goal of macros he aims to hit. He can use the meal planner function and add his goals in the form of minimum and maximum calories, max fats, max carbohydrates, and minimum protein. Then, we he finds plans his meals, he can add them to the meal planner and it will calculate for each macro if it falls within his goal or not and by how much, allowing him to better plan his meals in order to reach his macro goals. [Chris’ Story] 

Katherine often finds herself with leftover or miscellaneous ingredients in her fridge and pantry but doesn’t know what she can make with them. Instead of browsing recipes and checking what she’s missing, she wants the app to do the reverse: input what she has and get a list of recipe matches that use at least one of those ingredients, and the recipes are listed in decreasing order of matched ingredients. (or mostly those ingredients). [Rocky’s Story]  

Nolan has a set of recipes on display, either results from a filtered search or his saved recipes. He would like these recipes to be ordered according to different criteria. He clicks the sort by button, chooses a criterion, and the recipes displayed are reordered accordingly. [Owen’s story] 
