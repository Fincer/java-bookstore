# Java Bookstore Project

## Introduction

- This repository contains school-related Java server-side project work for an imaginary book store with required/necessary back-end features & course requirement description.

- This repository is linked to work of repository [java-server-programming - GitHub/Fincer](https://github.com/Fincer/java-server-programming).

- This repository is mirrored to [java-bookstore - Fjordtek Git/Fincer](https://fjordtek.com/git/Fincer/java-bookstore).

- this application has been deployed on [https://fjordtek.com/webapps/bookstore/booklist](https://fjordtek.com/webapps/bookstore/booklist). The following technical restrictions have been applied:

  - Credentials this application use are not connected to the main site credentials

  - Book list has maximum limit of `5` concurrent books

## Overview

### Front page

![](images/page_home.png)

_Front-end home page view with user sign-in form._

------------------------------

### Normal user view

![](images/page_user_front.png)

_Normal user view for the bookstore: get basic information of published books._

------------------------------

### Help desk view

![](images/page_helpdesk_front.png)

_Help desk view for the bookstore: view and edit published books in a limited way. No edit access to price & publication information._

------------------------------

### Sales manager view

![](images/page_salesmanager_front.png)

_Sales manager view for the bookstore: view, edit and add published and unpublished books._

------------------------------

### Administrative view

![](images/page_admin_front.png)

_Administrative view for the bookstore: view, edit, delete and add published and unpublished books. Access REST API._

------------------------------

### Database structure

![](images/db_structure.png)

_[Bookstore database structure](db_plans/bookstore-model.mwb)_

------------------------------

## Repository structure


| **Type** |        **Name**         |        **Description**       |
|----------|-------------------------|------------------------------|
| `Folder` | [bookstore](bookstore)  |  Main project folder (Maven) |
| `Folder` | [packaging](packaging)  |  OS-specific build scripts   |
| `Folder` | [documents](documents)  |  External project documents  |
| `Folder` |  [db_plans](db_plans)   |  SQL Database plans & drafts |
| `Folder` |    [images](images)     | Various screenshots & images |

### Recommendations

| **Software**  | **Version** |          **Description**          |
|---------------|-------------|-----------------------------------|
| Apache Tomcat |           9 | Web Server WAR package deployment |

## LICENSE

N/A (TBA)
