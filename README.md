Solution Document for Data Engineer Task
======
# Introduction:
I have `transactions.json` file that contains transaction data of users and a `api.py` REST api that needs to have two endpoints that will enrich the transaction data. 
For this task , I need to read a JSON file using python and enrich it with data provided by REST api to find high level aggregated sum.

### API Implementation
As per requirement for *user_status* endpoint , Client program will do a `GET` call similar to **-->** `/user_status/<user_id>?date=2017-10-10T10:00:00`
My task is to provide implementation of `get_status(self,user_id,date)` .
To implement this method: 
* I will loop `self.RECORDS` 
* do a match with  _user_id and date(timestamp as mentioned in question ,**if only date is required I can apply .getDate() method**)_
* if got matched will return _status from self.RECORDS_ and break the loop 
* else return _"non_paying"_.

For second endpoint `get_city`, Client program will do a `GET` call similar to **-->** `/ip_city/10.0.0.0` 
My task is to provide implementation of `get_city(self,ip)` .
I will use a **built-in module `ipaddress`** to implement this method because ip_address will help me to create IPV4/IPV6 objects from string that are comparable. It will make our task easier.
* create IP object `ip_to_check` from `ip` using ip_address
* loop `self.RANGES` to get City and list of IP range
* loop thru list of IP range to get start_ip and end_ip
* create IP object from start_ip and end_ip
* compare `ip_to_check` with start_ip and end_ip
* If it matches return City and break both loops
* else return _"unknown"_

### File Processing
In this task , I need to read a **_JSON_** file `transactions.log` and enrich it with two datapoints provided by API to get aggregated sum of `product_price` grouped by **_user_status_ and _user_city_**.

I have solved this problem using two approach:
1. Using Basic data structure of python i.e List and Dictionary (as goal was to evalute DS manipulation skills)
2. Using `pandas` library

#### Using Basic DS
To solve any programming task, I create sub task/module to focus on. It helps in designing solutions and make code easy to read. I have created 5 sub tasks to solve this problem.
1. Read _JSON_ file and create a List of JSON objects/dict.
2. Create a method to perform REST call for `user_status`
3. Create a method to perform REST call for `user_city`
4. Perform Required transformation on dataset i.e get aggregated sum of `product_price` grouped by **_user_status_ and _user_city_**.
5. Display output on console and write _JSON_ file.
> Find implementation in **`main.py`**

#### Using `pandas` library
I can simply use **pandas** library to read _JSON_ file in a dataframe and use **apply()** mehtod to get two additional column **_user_status_ and _user_city_** by calling REST API.
> Find implementation in **`main_pandas.py`**

## Scope of Improvement
While implement solution for this task, I felt we can improve this solution while working on large dataset. I would like to suggest some point of improvements for this problem:

* In `api.py` we can convert `RANGES` list into a sorted dictionary with key as _user_id_. This dictionary will contain another sorted sub-dictionary with _created_at_ key.This sub-dictionary will have a list of _JSON objects_ matching with key of sub_dictionary and parent dictionary. It will improve look-up time of REST cal for user_status.
* When working with a large datasets (such as 10 million user records), Doing REST call for each record will congest over network and makes our program slower. To avoid this case , we can introduce **REST calls for bulk data**. Client program can share list of arguments to REST API , It will process that request and return a list of _user_status/user_city_.

As my task was to provide implementation of methods, I didn't changed anything else from `api.py`.

## How to run program
First of all we need to run our REST API to recieve _user_status and user_city_
```python
python api.py
```
Now we have our API running, let's run our main script.
```python
python main.py
```
It will ask you file path of `transactions.log` file, If you are already in workspace folder just press _Enter_. Output will be printed on console and and also get saved in `output.json` file.

### To run program  `main_pandas.py`
If you don't have `pandas` library, You need to install it using below command:
```python
pip install pandas
```
Once you have `pandas` ready, We can run our script using :

```python
python main_pandas.py
```
Output will be printed on console and and also get saved in `pandas_output.json` file.

> I have provided doc string and comments in every _file_ that will help in understanding the code.
