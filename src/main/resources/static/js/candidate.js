var totalPages = 0;
var totalElements = 0;
var numberOfElements = 0;


const bindingDataTable = (url) => {
    $.ajax({
        url: url,
        method: "GET",
        dataType: "json"
    }).done((response) => {
        let tableRows = " ";
        totalPages = response.totalPages;
        totalElements = response.totalElements;
        numberOfElements = response.numberOfElements;
        if (response.content.length > 0) {
            response.content.forEach((candidate) => {
                tableRows += `
                            <tr>
                             <td>${candidate.fullName}</td>
                              <td>${candidate.email}</td>
                              <td>${candidate.phoneNumber}</td>
                              <td>${candidate.position}</td>
                              <td>${candidate.user}</td>
                              <td>${candidate.status}</td>
                              <td class="d-flex justify-content-between " style="font-size: 0.95rem;">
                                <a href="/candidate/info/${candidate.candidateId}" >
                                  <i class="mdi mdi-eye"></i></a>
                                <a href="/candidate/update/${candidate.candidateId}">
                                  <i class="mdi mdi-lead-pencil"></i>
                                </a>
                                <a class="text-danger" href="#" onclick="showMessage(${candidate.candidateId})">
                                 <i class="mdi mdi-delete"></i>
                                </a>
                              </td>
                            </tr>`;
            });
            renderPaging(response);
            $('tbody').html(tableRows);
        } else {
            renderPaging(response);
            $('tbody').html('<tr><td colspan="7">No item matches with your search data. Please try again.</td></tr>');
        }
        updatePaginationInfo();
    })
        .fail((xhr, status, error) => {
            console.error(`AJAX request failed. Status: ${status}, Error: ${error}`);
        }).always(() => {
        clickPagination();
    })
}

$("#recordPerPage").on("change", function () {
    const selectedRecordsPerPage = $(this).val();
    let searchText = document.getElementById("searchInput").value;
    var selectElement = document.getElementById("mySelect");
    var selectedOption = selectElement.options[selectElement.selectedIndex].value;


    if (searchText !== '' && selectedOption === '') {
        bindingDataTable(`/candidate/api/v1?keyword=${searchText}&pageSize=${selectedRecordsPerPage}`);
    } else if (selectedOption !== '' && searchText === '') {
        bindingDataTable(`/candidate/api/v1?status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else if (searchText !== '' && selectedOption !== '') {
        bindingDataTable(`/candidate/api/v1?keyword=${searchText}&status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else {
        bindingDataTable(`/candidate/api/v1?pageSize=${selectedRecordsPerPage}`);
    }
});



const renderPaging = (page) => {
    let pagging = ' ';
    if (page.totalPages <= 2) {
        for (let i = 0; i < page.totalPages; i++) {
            pagging += `<li class="page-item">
                <a class="page-link links pointer ${page.pageable.pageNumber === i ? 'bg-primary text-white' : ''}"
                     index="${i}">${i+1}</a></li>`;
        }
    } else {
        pagging +=
            `<li class="page-item">
                <a class="page-link pointer ${page.pageable.pageNumber == 0 ? 'd-none' : ''}"
                   index="${page.pageable.pageNumber - 1}" onclick="prevPage()" aria-label="Previous">
                    <span aria-hidden="true">Prev</span>
                </a>
            </li>`;

        const start = page.pageable.pageNumber > (page.totalPages - 2) ? page.totalPages - 2 : page.pageable.pageNumber;
        const end = page.pageable.pageNumber > (page.totalPages - 2) ? page.totalPages : page.pageable.pageNumber + 2;

        for (let i = start; i < end; i++) {
            pagging += `<li class="page-item">
                <a class="page-link links pointer ${page.pageable.pageNumber === i ? 'bg-primary active text-white' : ''}"
                     index="${i}">${i + 1}</a></li>`;
        }

        pagging += `<li class="page-item">
            <a class="page-link pointer
            ${page.pageable.pageNumber >= page.totalPages - 2 ? "d-none" : ""}"
               index="${page.pageable.pageNumber + 1}" onclick="nextPage()" aria-label="Next">
                <span aria-hidden="true">Next</span>
            </a>
        </li>`;
    }
    $('.pagination').html(pagging);
}
const clickPagination = () => {
    $(".links").on("click", (e) => {
        const target = e.target;
        const id = $(target).attr("index");
        var selectRow = document.getElementById("recordPerPage");
        // Lấy giá trị của option được chọn
        var selectedRecordsPerPage = selectRow.value;

        let searchText = document.getElementById("searchInput").value;
        var selectElement = document.getElementById("mySelect");
        var selectedOption = selectElement.options[selectElement.selectedIndex].value;

        if (searchText !== '' && selectedOption === '') {
            bindingDataTable(`/candidate/api/v1?pageNo=${id}&keyword=${searchText}&pageSize=${selectedRecordsPerPage}`);
        } else if (selectedOption !== '' && searchText === '') {
            bindingDataTable(`/candidate/api/v1?pageNo=${id}&status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
        } else if (searchText !== '' && selectedOption !== '') {
            bindingDataTable(`/candidate/api/v1?pageNo=${id}&keyword=${searchText}&status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
        } else {
            bindingDataTable(`/candidate/api/v1?pageNo=${id}&pageSize=${selectedRecordsPerPage}`);
        }
    })
}
const prevPage = () => {
    const currentPage = parseInt($('.pagination .active').attr('index'));
    const prevPage = parseInt(currentPage) - 1;

    let searchText = document.getElementById("searchInput").value;
    var selectElement = document.getElementById("mySelect");
    var selectedOption = selectElement.options[selectElement.selectedIndex].value;
    var selectRow = document.getElementById("recordPerPage");
    var selectedRecordsPerPage = selectRow.value;

    if (searchText !== '' && selectedOption === '') {
        bindingDataTable(`/candidate/api/v1?pageNo=${prevPage}&keyword=${searchText}&pageSize=${selectedRecordsPerPage}`);
    } else if (selectedOption !== '' && searchText === '') {
        bindingDataTable(`/candidate/api/v1?pageNo=${prevPage}&status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else if (searchText !== '' && selectedOption !== '') {
        bindingDataTable(`/candidate/api/v1?pageNo=${prevPage}&keyword=${searchText}&status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else {
        bindingDataTable(`/candidate/api/v1?pageNo=${prevPage}&pageSize=${selectedRecordsPerPage}`);
    }
};

const nextPage = () => {

    const currentPage = parseInt($('.pagination .active').attr('index'));
    const nextPage = parseInt(currentPage) + 1;
    var selectRow = document.getElementById("recordPerPage");
    var selectedRecordsPerPage = selectRow.value;
    let searchText = document.getElementById("searchInput").value;
    var selectElement = document.getElementById("mySelect");
    var selectedOption = selectElement.options[selectElement.selectedIndex].value;

    if (searchText !== '' && selectedOption === '') {
        bindingDataTable(`/candidate/api/v1?pageNo=${nextPage}&keyword=${searchText}&pageSize=${selectedRecordsPerPage}`);
    } else if (selectedOption !== '' && searchText === '') {
        bindingDataTable(`/candidate/api/v1?pageNo=${nextPage}&status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else if (searchText !== '' && selectedOption !== '') {
        bindingDataTable(`/candidate/api/v1?pageNo=${nextPage}&keyword=${searchText}&status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else {
        bindingDataTable(`/candidate/api/v1?pageNo=${nextPage}&pageSize=${selectedRecordsPerPage}`);
    }

};


function searchText(event) {
    event.preventDefault();
    let searchText = document.getElementById("searchInput").value;
    var selectElement = document.getElementById("mySelect");
    var selectedOption = selectElement.options[selectElement.selectedIndex].value;
    var selectRow = document.getElementById("recordPerPage");
    // Lấy giá trị của option được chọn
    var selectedRecordsPerPage = selectRow.value;

    if (searchText !== '' && selectedOption === '') {
        bindingDataTable(`/candidate/api/v1?keyword=${searchText}&pageSize=${selectedRecordsPerPage}`);
    } else if (selectedOption !== '' && searchText === '') {
        bindingDataTable(`/candidate/api/v1?status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else if (searchText !== '' && selectedOption !== '') {
        bindingDataTable(`/candidate/api/v1?keyword=${searchText}&status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else {
        bindingDataTable(`/candidate/api/v1?pageSize=${selectedRecordsPerPage}`);
    }

}
function updatePaginationInfo(){
    $('#zero_config_info').text(`Show ${numberOfElements} / ${totalElements} rows`);
}

bindingDataTable('/candidate/api/v1?pageSize=2');

function showMessage(id) {
    var option = confirm('Are you sure you want to delete this candidate');
    if (option === true) {
        window.location.href = "/candidate/delete?candidateId=" + id;
    }
}

function banCandidate(id, event) {
    var option = confirm('Are you sure you want to ban this candidate');
    event.preventDefault();
    if (option === true) {
        window.location.href = "/candidate/ban/" + id;
    }
}

function openCandidate(id) {
    var option = confirm('Are you sure you want to open this candidate');
    if (option === true) {
        window.location.href = "/candidate/open/" + id;
    }
}

function deleteFile(id) {
    var option = confirm('Are you sure you want to delete this file');
    if (option === true) {
        window.location.href = "/candidate/" + id + "/files/delete";
    }
}


document.getElementById('assign-me').addEventListener("click", function (ev) {
    ev.preventDefault();
    var assignedRecruiter = this.getAttribute('data');
    var recruiterSelect = document.getElementById('user');
    var recruiterValue = document.getElementById('select2-user-container');
    recruiterValue.innerText = assignedRecruiter;
    var options = recruiterSelect.options;
    for (var i = 0; i < options.length; i++) {
        if (options[i].value === assignedRecruiter) {
            console.log(recruiterValue)
            options[i].selected = true;
            recruiterSelect.value = options[i].value;
            break;
        }
    }
});