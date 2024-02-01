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
            response.content.forEach((user) => {
                tableRows += `
                            <tr>
                              <td>${user.userName}</td>
                              <td>
                                  <a href="mailto:${user.email}">
                                  ${user.email}                          
                                  </a>
                              </td>
                              <td>
                                <a href="tel:${user.phoneNumber}"> ${user.phoneNumber}</a>
                               </td>
                              <td>${user.role}</td>
                              <td>${user.status}</td>
                              <td class="d-flex justify-content-around" style="font-size: 0.95rem;">                        
                                <a  href="/user/view/${user.userId}"  class="view-user">
                                <i class="mdi mdi-eye"></i>
                                </a>
                                <a href="/user/edit/${user.userId}">
                                  <i class="mdi mdi-lead-pencil"></i>
                                </a>
                              </td>
                            </tr>`;
            });
            renderPaging(response);
            $('tbody').html(tableRows);
        } else {
            totalPages = response.totalPages;
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

function updatePaginationInfo() {
    $('#zero_config_info').text(`Show ${numberOfElements} / ${totalElements} rows`);
}

const renderPaging = (page) => {
    let pagging = ' ';
    if (totalPages <= 2) {
        for (let i = 0; i < totalPages; i++) {
            pagging += `<li class="page-item">
                <a class="page-link links pointer ${page.pageable.pageNumber === i ? 'bg-primary active text-white' : ''}"
                     index="${i}">${i + 1}</a></li>`;
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
            <a class="page-link  pointer
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
        let searchText = document.getElementById("searchInput").value;
        var selectElement = document.getElementById("mySelect");
        var selectedOption = selectElement.options[selectElement.selectedIndex].value;
        var selectRow = document.getElementById("recordPerPage");
        // Lấy giá trị của option được chọn
        var selectedRecordsPerPage = selectRow.value;


        if (searchText !== '' && selectedOption === '') {
            bindingDataTable(`/user/api/v1?pageNo=${id}&pageSize=${selectedRecordsPerPage}&keyword=${searchText}`);
        } else if (selectedOption !== '' && searchText === '') {
            bindingDataTable(`/user/api/v1?pageNo=${id}&pageSize=${selectedRecordsPerPage}&role=${selectedOption}`);
        } else if (searchText !== '' && selectedOption !== '') {
            bindingDataTable(`/user/api/v1?pageNo=${id}&keyword=${searchText}&role=${selectedOption}`);
        } else {
            bindingDataTable(`/user/api/v1?pageNo=${id}&pageSize=${selectedRecordsPerPage}`);
        }
    })
}

$("#recordPerPage").on("change", function () {
    const selectedRecordsPerPage = $(this).val();
    let searchText = document.getElementById("searchInput").value;
    var selectElement = document.getElementById("mySelect");
    var selectedOption = selectElement.options[selectElement.selectedIndex].value;


    if (searchText !== '' && selectedOption === '') {
        bindingDataTable(`/user/api/v1?pageSize=${selectedRecordsPerPage}&keyword=${searchText}`);
    } else if (selectedOption !== '' && searchText === '') {
        bindingDataTable(`/user/api/v1?pageSize=${selectedRecordsPerPage}&role=${selectedOption}`);
    } else if (searchText !== '' && selectedOption !== '') {
        bindingDataTable(`/user/api/v1?pageSize=${selectedRecordsPerPage}&keyword=${searchText}&role=${selectedOption}`);
    } else {
        bindingDataTable(`/user/api/v1?pageSize=${selectedRecordsPerPage}`);
    }
});

function searchText(event) {
    event.preventDefault();
    let searchText = document.getElementById("searchInput").value;
    var selectElement = document.getElementById("mySelect");
    var selectedOption = selectElement.options[selectElement.selectedIndex].value;
    var selectRow = document.getElementById("recordPerPage");
    // Lấy giá trị của option được chọn
    var selectedRecordsPerPage = selectRow.value;

    if (searchText !== '' && selectedOption === '') {
        bindingDataTable(`/user/api/v1?keyword=${searchText}&pageSize=${selectedRecordsPerPage}`);
    } else if (selectedOption !== '' && searchText === '') {
        bindingDataTable(`/user/api/v1?role=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else if (searchText !== '' && selectedOption !== '') {
        bindingDataTable(`/user/api/v1?keyword=${searchText}&role=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else {
        bindingDataTable(`/user/api/v1?pageSize=${selectedRecordsPerPage}`);
    }

}


const prevPage = () => {
    const currentPage = parseInt($('.pagination .active').attr('index'));
    const prevPage = parseInt(currentPage) - 1;
    var selectRow = document.getElementById("recordPerPage");
    // Lấy giá trị của option được chọn
    var selectedRecordsPerPage = selectRow.value;
    let searchText = document.getElementById("searchInput").value;
    var selectElement = document.getElementById("mySelect");
    var selectedOption = selectElement.options[selectElement.selectedIndex].value;

    if (searchText !== '' && selectedOption === '') {
        bindingDataTable(`/user/api/v1?pageNo=${prevPage}&keyword=${searchText}&pageSize=${selectedRecordsPerPage}`);
    } else if (selectedOption !== '' && searchText === '') {
        bindingDataTable(`/user/api/v1?pageNo=${prevPage}&status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else if (searchText !== '' && selectedOption !== '') {
        bindingDataTable(`/user/api/v1?pageNo=${prevPage}&keyword=${searchText}&status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else {
        bindingDataTable(`/user/api/v1?pageNo=${prevPage}&pageSize=${selectedRecordsPerPage}`);
    }
};

const nextPage = () => {

    const currentPage = parseInt($('.pagination .active').attr('index'));
    const nextPage = parseInt(currentPage) + 1;

    let searchText = document.getElementById("searchInput").value;
    var selectElement = document.getElementById("mySelect");
    var selectedOption = selectElement.options[selectElement.selectedIndex].value;
    var selectRow = document.getElementById("recordPerPage");
    // Lấy giá trị của option được chọn
    var selectedRecordsPerPage = selectRow.value;
    if (searchText !== '' && selectedOption === '') {
        bindingDataTable(`/user/api/v1?pageNo=${nextPage}&keyword=${searchText}&pageSize=${selectedRecordsPerPage}`);
    } else if (selectedOption !== '' && searchText === '') {
        bindingDataTable(`/user/api/v1?pageNo=${nextPage}&status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else if (searchText !== '' && selectedOption !== '') {
        bindingDataTable(`/user/api/v1?pageNo=${nextPage}&keyword=${searchText}&status=${selectedOption}&pageSize=${selectedRecordsPerPage}`);
    } else {
        bindingDataTable(`/user/api/v1?pageNo=${nextPage}&pageSize=${selectedRecordsPerPage}`);
    }

};

bindingDataTable('/user/api/v1?pageSize=2');


