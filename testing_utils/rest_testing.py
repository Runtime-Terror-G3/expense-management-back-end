import requests
import json
import hashlib

base_url = 'http://127.0.0.1:8080/api/expense-management'

#createUser('username@domain', hashlib.sha256(b'pass').digest().hex(),
#   'example', 'user', '1970-01-01')
def createUser(email, password, first_name, last_name, birth_day):
    response = requests.post(base_url + '/create-account',
        data = json.dumps(
        {
            'email': email,
            'password': password,
            'firstName': first_name,
            'lastName': last_name,
            'dateOfBirth': birth_day
        }).encode())

    print(str(response.status_code) + ": " + response.text)

# Returns a token
#signIn('username@domain', hashlib.sha256(b'pass').digest().hex())
def signIn(email, password):
    response = requests.post(base_url + '/sign-in',
            data = json.dumps(
            {
                'email': email,
                'password': password
            }).encode())

    print(str(response.status_code) + ": " + response.text)
    token = json.loads(response.text)["token"]

    return token

# ---------- Expense functions ----------

#addExpense(1.1, 'Food', '1970-01-01T00:00:00Z', token)
def addExpense(amount, category, date, token):
    response = requests.post(base_url + '/add-expense',
            data = json.dumps(
            {
                'amount': amount,
                'category': category,
                'date': date
            }).encode(),
            headers = {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token})

    print(str(response.status_code) + ": " + response.text)

#deleteExpense(1, token)
def deleteExpense(expense_id, token):
    response = requests.delete(
            base_url + '/delete-expense/' + str(expense_id),
            headers = {'Authorization': 'Bearer ' + token})

    print(str(response.status_code) + ": " + response.text)

#getExpenses('Food', 0, 2145823199, token)
def getExpenses(category, start, end, token):
    response = requests.get(
            base_url + '/get-expenses',
            params = {
                'category': category,
                'startDate': start,
                'endDate': end},
            headers = {'Authorization': 'Bearer ' + token})

    print(str(response.status_code) + ": " + response.text)

#updateExpense(1, 1.1, 'Food', '1984-01-01T00:00:00Z', token)
def updateExpense(expense_id, amount, category, date, token):
    response = requests.post(
            base_url + '/update-expense/' + str(expense_id),
            data = json.dumps(
            {
                'amount': amount,
                'category': category,
                'date': date
            }).encode(),
            headers = {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token})

    print(str(response.status_code) + ": " + response.text)

#totalExpensesInTime('day', 'Food', '1970-01-01', '2037-12-30', token)
def totalExpensesInTime(granularity, category, start, end, token):
    response = requests.get(
            base_url + '/total-expenses-in-time',
            params = {
                'granularity': granularity,
                'category': category,
                'startDate': start,
                'endDate': end},
            headers = {'Authorization': 'Bearer ' + token})

    print(str(response.status_code) + ": " + response.text)

#categoryTotal('1970-01-01T00:00:00Z', '2037-12-30T23:59:59Z', token)
def categoryTotal(start, end, token):
    response = requests.get(
            base_url + '/category-total',
            params = {
                'start': start,
                'end': end},
            headers = {'Authorization': 'Bearer ' + token})

    print(str(response.status_code) + ": " + response.text)

# ---------- Monthly budget functions ----------

#addMonthlyBudget(123, '1970-01-01T00:00:00Z', token)
def addMonthlyBudget(income, date, token):
    response = requests.post(base_url + '/add-monthly-budget',
            data = json.dumps(
            {
                'income': income,
                'date': date
            }).encode(),
            headers = {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token})

    print(str(response.status_code) + ": " + response.text)

#deleteMonthlyBudget(1, token)
def deleteMonthlyBudget(budget_id, token):
    response = requests.delete(
            base_url + '/delete-monthly-budget/' + str(budget_id),
            headers = {'Authorization': 'Bearer ' + token})

    print(str(response.status_code) + ": " + response.text)

#getMonthlyBudgets('1970-01-01', '2037-12-30', token)
def getMonthlyBudgets(start, end, token):
    response = requests.get(
            base_url + '/get-monthly-budgets',
            params = {
                'startDate': start,
                'endDate': end},
            headers = {'Authorization': 'Bearer ' + token})

    print(str(response.status_code) + ": " + response.text)

#updateMonthlyBudget(1, 321, '1970-01-01T02:00:00Z', token)
def updateMonthlyBudget(budget_id, income, date, token):
    response = requests.put(
            base_url + '/update-monthly-budget/' + str(budget_id),
            data = json.dumps(
            {
                'income': income,
                'date': date
            }).encode(),
            headers = {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token})

    print(str(response.status_code) + ": " + response.text)

# ---------- Wishlist functions ----------

#addWishlistItem('item', 123, 'https://store.asd/item',
#   'https://store.asd/item-image', 'Altex', token)
def addWishlistItem(title, price, link, image, vendor, token):
    response = requests.post(base_url + '/add-wishlistItem',
            data = json.dumps(
            {
                'title': title,
                'price': price,
                'link': link,
                'image': image,
                'vendor': vendor
            }).encode(),
            headers = {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token})

    print(str(response.status_code) + ": " + response.text)

#getWishlistItems(token)
def getWishlistItems(token):
    response = requests.get(
            base_url + '/get-wishlist-items',
            headers = {'Authorization': 'Bearer ' + token})

    print(str(response.status_code) + ": " + response.text)

if __name__ == '__main__':
    password = hashlib.sha256(b'asd').digest().hex()
    email = 'first@dsa.org'

    token = signIn(email, password)
