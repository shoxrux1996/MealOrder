# Meal Order Application Project

## 1 Objective

In this project we want to create an application which manages the orders of meals from clients. This is
the 3rd options for final project of this course. There will be two type of user should be in application

1. The first one to order a meal to receiver user (or admin)
2. The receiver app is the second application which managed the orders

## 2 Description

### 2.1 User Interface

```
When the user first time open the application, they should be authenticated (login or register)
```
```
The main user interface contains 1 main activity and 3 fragments inside the activity
```
- MainActivity
    o Menu Fragment
    o Favorite Fragment
    o Profile Fragment


### 2.2 User Functions

1. If you click a meal card view in Menu page. One should able to see the description about the meal

```
Then if the floating action button is clicked, the user could see a
form to order the meal.
```

And after the ORDER button is clicked the order should be sent to the order receiver application.

2. In the profile page, user should be
    able to see his basic information
    and orders list button, if he clicks
    the order list button order list
    activity should be appeared in the
    screen.

### 2.3 Admin Interface

```
The admin user should be able to
insert meals, check the orders and decide to accept or reject it. Admin also goes through same
authentication process.
The admin user interface 1 main activity and 3 fragments inside the activity.
```
- AdminActivity:
    o Users Fragment
    o Menus Fragment
    o Orders Fragment


### 2.4 Admin Function

1. Admin can do all CRUD (Create, Read, Update, Delete) actions on menus:
2. Received orders status are marked in AdminOrder Fragment.

```
If Admin status of order must be changed, it also can be done
in the same activity.
```

## 3 Extra Credit

```
 To make better user friendly application We have added some UX features to given User Interface:
```
- Confirmation/Success dialogs (to prevent user errors/to acknowledge done action)
- Scroll Views, aimsOption (to simplify entering the text)
 Using database to save memo contents insisted of files.
- Firebase Auth:
Login, Register, Get Current User
- Firebase Storage:
Upload/Download: User and Menu/Favorites images
- Firebase Database:
Storing and Retrieving: Users info, Menus, Orders, Favorites
Listeners: Creation/Updating/Removing Users info, Menus, Orders, Favorites

## 4 Code Structure

```
Authors: Activities : Adapters : Fragments: Models
```
```
Suhrob Askarov
```
```
LoginActivity
RegisterActivity
MainActivity
```
```
ProfileAdapter
Tab3Profile
Client
```
Erkinboy Botirov

```
MakeOrderActivity
MenuInfoActivity
OrderListActivity
```
```
FavoriteRecyclerViewAdapter
MenuRecyclerViewAdapter
OrderRecyclerViewAdapter
```
```
Tab1Favorite
Tab2Menu
Favorite
```
Shokhrukh
Shomakhmudov

```
AdminActivity
AdminMenuCreateActivity
AdminMenuInfoActivity
AdminOrderInfoActivity
```
```
AdminMenuRecyclerViewAdapter
AdminOrderRecyclerViewAdapter
AdminUserRecyclerViewAdapter
```
```
Tab1AdminUsers
Tab2AdminMenus
Tab3AdminOrders
```
```
Menu
Order
```

