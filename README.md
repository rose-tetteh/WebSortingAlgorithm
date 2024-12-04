
# Advanced Web Sorting Tool - Design Documentation

## 1. Overview
The project is a web application that allows users to create datasets of numbers, sort them using various algorithms, and manage these datasets (create, delete, sort). It provides a RESTful API for interacting with the datasets. The backend is implemented using **Spring** (not Spring Boot), and different sorting algorithms are implemented for handling datasets.

## 2. Architecture Overview
The application is structured with a layered architecture, typical for Spring applications:

- **Controller Layer**: Manages incoming HTTP requests and directs them to appropriate service methods.
- **Service Layer**: Handles the business logic, including dataset manipulation and sorting.
- **Model Layer**: Contains data representations (such as `DataModelDto` and `SortRequestDto`).
- **Persistence Layer**: Data is stored in memory using a `Map` (`dataStore`), simulating a database.

### System Architecture Diagram
```plaintext
  +----------------+      +-------------------+      +-----------------+
  |   Web Browser  |----->|  Spring App        |----->|   Sorting Logic  |
  |    (Frontend)  |      | (Controller +      |      |   (Algorithms)   |
  |                |<---->|   Service Layers)  |<---->|   (QuickSort,    |
  +----------------+      +-------------------+      |   MergeSort, etc.)|
                             |                       +-----------------+
                             |
                             v
                   +---------------------+
                   | In-Memory DataStore |
                   | (Dataset Storage)   |
                   +---------------------+
```

## 3. Components & Description

### 3.1 Controller Layer
The `Controller` class is responsible for exposing RESTful endpoints to handle user requests. It delegates the logic to the `DataService` for actual processing.

- **Endpoints**:
    - `/api/v1/data`: Fetches all datasets.
    - `/api/v1/data/{id}`: Fetches a specific dataset by its ID.
    - `/api/v1/algorithm/{algorithm}`: Fetches datasets sorted by a specific algorithm.
    - `/api/v1/data/add`: Creates a new dataset.
    - `/api/v1/sort`: Sorts a specific dataset based on the requested algorithm.
    - `/api/v1/data/{id}/delete`: Deletes a dataset by ID.

**HATEOAS Links**:
The `HateoasGenerator` uses HATEOAS principles to enrich the API responses with links to other relevant resources (e.g., `all-datasets`).

### 3.2 Service Layer
The `DataService` class contains the business logic and is responsible for handling dataset operations. It manages the in-memory storage (`dataStore`), sorts datasets, and performs CRUD operations.

- **Methods**:
    - `getAllData()`: Returns all datasets.
    - `getDataById(id)`: Returns a specific dataset by its ID.
    - `createData(list)`: Creates a new dataset and stores it in `dataStore`.
    - `deleteData(id)`: Deletes a dataset by its ID.
    - `getDataByAlgorithm(algorithm)`: Filters datasets based on the sorting algorithm used.
    - `sort(sortRequest)`: Sorts a dataset using the algorithm specified in the request. It delegates the sorting task to different sorting algorithms.

The service uses an `AtomicInteger` for generating unique IDs for datasets and stores datasets in an in-memory `HashMap` (`dataStore`).

### 3.3 Sorting Logic
The sorting algorithms are implemented as separate classes:
- `QuickSort`: Implements the QuickSort algorithm.
- `HeapSort`: Implements the HeapSort algorithm.
- `MergeSort`: Implements the MergeSort algorithm.
- `RadixSort`: Implements the RadixSort algorithm.
- `BucketSort`: Implements the BucketSort algorithm.

Each algorithm class implements a `SortAlgorithm` interface and provides its own `sort()` method.

### 3.4 Data Model Layer
- **DataModel**:
    - Represents a dataset.
    - Attributes:
        - `id`: Unique identifier for the dataset.
        - `list`: List of integers representing the dataset.
        - `sortedList`: The dataset after it has been sorted.
        - `sortAlgorithm`: The algorithm used to sort the dataset.

- **SortRequest**:
    - Represents a request to sort a dataset.
    - Attributes:
        - `id`: ID of the dataset to sort.
        - `algorithm`: The sorting algorithm to use.

## 4. Database and Data Storage
In this design, an in-memory `Map` (`dataStore`) is used to simulate a database for storing datasets. The key is the dataset ID, and the value is the `DataModel` object. This setup is useful for prototyping but should be replaced with a proper database in a production system.

### Data Model Structure
```java
public class DataModel {
    private int id;                  // Unique dataset ID
    private List<Integer> list;      // List of numbers to sort
    private List<Integer> sortedList;// Sorted version of the list
    private String sortAlgorithm;    // Algorithm used for sorting
}
```

### Sort Request Structure
```java
public class SortRequest {
    private int id;                 // ID of the dataset to sort
    private String algorithm;       // Sorting algorithm name
}
```

## 5. Error Handling
- **NoSuchElementException**: Thrown if an operation tries to access a non-existent dataset by ID.
- **IllegalArgumentException**: Thrown if an unsupported sorting algorithm is specified.
- **404 Not Found**: Returned if a dataset or algorithm cannot be found.
- **400 Bad Request**: Returned if the request is missing required parameters or contains invalid values.

## 6. Interaction Flow
1. **Creating a Dataset**:
    - The user submits a list of integers via the `POST /data/add` endpoint.
    - The backend processes the list, assigns a unique ID, and returns the created dataset.

2. **Sorting a Dataset**:
    - The user selects an algorithm and submits a `POST /sort` request with the dataset ID and selected algorithm.
    - The backend validates the request, performs the sorting, and returns the sorted dataset.

3. **Deleting a Dataset**:
    - The user deletes a dataset by submitting a `DELETE /data/{id}/delete` request with the dataset ID.
    - The backend removes the dataset from memory.

4. **Fetching Datasets**:
    - The user can fetch datasets by ID, algorithm, or all datasets using the appropriate GET endpoints.

## 7. Future Improvements
- **Persistence**: Switch from in-memory data storage to a relational database for production environments.
- **Sorting Optimization**: Allow users to customize sorting parameters, such as recursion depth for QuickSort.
- **Frontend Interface**: Develop a rich frontend interface to interact with the backend API, allowing for dataset creation, sorting, and management directly from the UI.

