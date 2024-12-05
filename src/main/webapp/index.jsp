<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <title>Sorting Algorithms Visualization</title>
    <style>
        body {
            background-color: #f8f9fa;
        }
        .result-box {
            background-color: #ffffff;
            border: 1px solid #ced4da;
            border-radius: 0.5rem;
            padding: 1rem;
            margin-top: 1rem;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }
        .list-view {
            word-break: break-all;
        }
        .dataset-card {
            margin-bottom: 10px;
        }
        .dataset-details {
            display: none;
            margin-top: 10px;
            background-color: #f1f3f5;
            padding: 10px;
            border-radius: 5px;
        }
        .expand-btn {
            cursor: pointer;
            margin-left: 10px;
            font-size: 1.2em;
            user-select: none;
        }
        .details-container {
            margin-top: 10px;
        }
    </style>
</head>
<body>
<div class="container my-5">
    <h1 class="text-center mb-4">Advanced Web Sorting Tool</h1>

    <div class="row">
        <div class="col-md-6">
            <div class="card mb-4">
                <div class="card-header">
                    <h4>Create Dataset</h4>
                </div>
                <div class="card-body">
                    <form id="dataForm">
                        <div class="mb-3">
                            <label for="dataList" class="form-label">Enter Numbers (comma-separated):</label>
                            <input type="text" class="form-control" id="dataList"
                                   placeholder="Example: 5, 3, 8, 1, 9" required>
                        </div>
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-plus-circle"></i> Create Dataset
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card mb-4">
                <div class="card-header">
                    <h4>Sort Dataset</h4>
                </div>
                <div class="card-body">
                    <form id="sortForm">
                        <div class="mb-3">
                            <label for="datasetId" class="form-label">Select Dataset:</label>
                            <select class="form-select" id="datasetId" required>
                                <option value="">Choose a dataset</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="algorithm" class="form-label">Sorting Algorithm:</label>
                            <select class="form-select" id="algorithm" required>
                                <option value="quickSort">QuickSort</option>
                                <option value="mergeSort">MergeSort</option>
                                <option value="heapSort">HeapSort</option>
                                <option value="radixSort">RadixSort</option>
                                <option value="bucketSort">BucketSort</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-sort-amount-down"></i> Sort Dataset
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header">
                    <h4>Results</h4>
                </div>
                <div class="card-body" id="resultBox">
                    <p class="text-muted">Sorting results will be displayed here</p>
                </div>
            </div>
        </div>
    </div>

    <div class="row mt-4">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header">
                    <h4>Available Datasets</h4>
                </div>
                <div class="card-body" id="datasetList">
                    <p class="text-muted">No datasets available</p>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    $(document).ready(function () {
        const BASE_URL = 'http://localhost:8080/WebSortingAlgorithm_war/api/v1';

        function createDatasetDetailsHTML(dataset) {
            return `
                <div class="details-container">
                    <div class="row">
                        <div class="col-md-4">
                            <strong>Unsorted List:</strong>
                            <p>${dataset.list.join(', ')}</p>
                        </div>
                        <div class="col-md-4">
                            <strong>Sorted List:</strong>
                            <p>${dataset.sortedList ? dataset.sortedList.join(', ') : 'Not sorted yet'}</p>
                        </div>
                    </div>
                </div>
            `;
        }

        function loadDatasets() {
            $.ajax({
                url: BASE_URL + '/data',
                method: 'GET',
                success: function(datasets) {
                    const $datasetSelect = $('#datasetId');
                    const $datasetList = $('#datasetList');

                    $datasetSelect.empty().append('<option value="">Choose a dataset</option>');
                    $datasetList.empty();

                    if (datasets && datasets.length > 0) {
                        datasets.forEach(function(dataset) {
                            // Add dataset to dropdown
                            $datasetSelect.append(`
                                <option value="${dataset.id}">
                                    Dataset ${dataset.id}: ${dataset.list.join(', ')}
                                </option>
                            `);


                            $datasetList.append(`
                                <div class="card mb-2">
                                    <div class="card-header d-flex justify-content-between align-items-center">
                                        <div>
                                            <span>Dataset ${dataset.id}: ${dataset.list.join(', ')}</span>
                                            <span class="expand-btn text-primary" data-id="${dataset.id}">
                                                &#9660;
                                            </span>
                                        </div>
                                        <button class="btn btn-sm btn-danger delete-dataset" data-id="${dataset.id}">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </div>
                                    <div id="dataset-details-${dataset.id}" class="card-body dataset-details">
                                        ${createDatasetDetailsHTML(dataset)}
                                    </div>
                                </div>
                            `);
                        });


                        $('.expand-btn').on('click', function() {
                            const datasetId = $(this).data('id');
                            const $details = $(`#dataset-details-${datasetId}`);
                            const $btn = $(this);

                            if ($details.is(':visible')) {
                                $details.slideUp();
                                $btn.html('&#9660;');
                            } else {
                                $details.slideDown();
                                $btn.html('&#9650;');
                            }
                        });
                    } else {
                        $datasetList.html('<p class="text-muted">No datasets available</p>');
                    }
                },
                error: function() {
                    $('#resultBox').html(`
                        <div class="alert alert-danger">
                            Failed to load datasets
                        </div>
                    `);
                }
            });
        }

        // Create Dataset
        $('#dataForm').submit(function(event) {
            event.preventDefault();
            const dataList = $('#dataList').val().split(',').map(x => parseInt(x.trim()));

            $.ajax({
                url: BASE_URL + '/data/add',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(dataList),
                success: function(dataset) {
                    loadDatasets();

                    $('#dataList').val('');
                    $('#resultBox').html(`
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            Dataset ${dataset.id} created successfully
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    `);
                },
                error: function() {
                    $('#resultBox').html(`
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            Failed to create dataset
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    `);
                }
            });
        });

        // Sort Dataset
        $('#sortForm').submit(function(event) {
            event.preventDefault();
            const datasetId = $('#datasetId').val();
            const algorithm = $('#algorithm').val();

            $.ajax({
                url: BASE_URL + '/sort',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    id: datasetId,
                    algorithm: algorithm
                }),
                success: function(response) {
                    $('#resultBox').html(`
                        <div class="alert alert-success">
                            <h4>Sorting Results</h4>
                            <div class="row">
                                <div class="col-md-6">
                                    <h5>Unsorted Dataset</h5>
                                    <p><strong>Dataset ID:</strong> ${response.id}</p>
                                    <p><strong>Original Data:</strong> ${response.list.join(', ')}</p>
                                </div>
                                <div class="col-md-6">
                                    <h5>Sorted Dataset</h5>
                                    <p><strong>Algorithm:</strong> ${algorithm}</p>
                                    <p><strong>Sorted Data:</strong> ${response.sortedList.join(', ')}</p>
                                </div>
                            </div>
                        </div>
                    `);

                    loadDatasets();
                },
                error: function() {
                    $('#resultBox').html(`
                        <div class="alert alert-danger">
                            Sorting failed
                        </div>
                    `);
                }
            });
        });

        // Delete Dataset
        $(document).on('click', '.delete-dataset', function() {
            const datasetId = $(this).data('id');

            $.ajax({
                url: BASE_URL + `/data/${datasetId}/delete`,
                method: 'DELETE',
                success: function() {
                    loadDatasets();
                    $('#resultBox').html(`
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            Dataset deleted successfully
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    `);
                },
                error: function() {
                    $('#resultBox').html(`
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            Failed to delete dataset
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    `);
                }
            });
        });

        loadDatasets();
    });
</script>
</body>
</html>