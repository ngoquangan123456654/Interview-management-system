var totalPages = 0;
var totalElements = 0;
var numberOfElements = 0;
const bindingDataTable = (url) => {
    $.ajax({
        url: url,
        method: "GET",
        dataType: "json"
    }).done((response) => {
        console.log(response)
        let tableRows = " ";
        if (response.content.length > 0) {
            totalPages = response.totalPages;
            totalElements = response.totalElements;
            numberOfElements = response.numberOfElements;
            response.content.forEach((offer) => {
                tableRows += `
                            <tr>
             <td>${offer.candidateName}</td>
             <td>${offer.emailCandidate}</td>
             <td>${offer.approver}</td>
             <td>${offer.department}</td>
             <td>${offer.note}</td>
             <td>${offer.status}</td>
             <td>
             <a href="/offer/view/${offer.offerDtoId}"><i class="mdi mdi-eye"></i></a>
             <a href="/offer/edit/${offer.offerDtoId}"><i class="mdi mdi-lead-pencil"></i></a>
             </td>
             </tr>`;
            });
            renderPaging(response);
            updatePaginationInfo();
            $('tbody').html(tableRows);
        } else {
            totalPages = response.totalPages;
            renderPaging(response);
            $('tbody').html('<tr><td colspan="7">No item matches with your search data. Please try again.</td></tr>');
        }
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
    if (totalPages <= 4) {
        for (let i = 0; i < totalPages; i++) {
            pagging += `<li class="page-item">
                <a class="page-link links ${page.pageable.pageNumber === i ? 'bg-primary active text-white' : ''}"
                     index="${i}">${i + 1}</a></li>`;
        }
    } else {
        pagging +=
            `<li class="page-item">
                <a class="page-link ${page.pageable.pageNumber == 0 ? 'd-none' : ''}"
                   index="${page.pageable.pageNumber - 1}" onclick="prevPage()" aria-label="Previous">
                    <span aria-hidden="true">Prev</span>
                </a>
            </li>`;

        const start = page.pageable.pageNumber > (page.totalPages - 4) ? page.totalPages - 4 : page.pageable.pageNumber;
        const end = page.pageable.pageNumber > (page.totalPages - 4) ? page.totalPages : page.pageable.pageNumber + 4;

        for (let i = start; i < end; i++) {
            pagging += `<li class="page-item">
                <a class="page-link links ${page.pageable.pageNumber === i ? 'bg-primary active text-white' : ''}"
                     index="${i}">${i + 1}</a></li>`;
        }

        pagging += `<li class="page-item">
            <a class="page-link 
            ${page.pageable.pageNumber >= page.totalPages - 4 ? "d-none" : ""}"
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
        //thêm index vào api nữa
        let searchText = document.getElementById("search-input").value;
        let selectedField = document.querySelector('#selectField').value;
        let selectedStatus = document.querySelector('#selectedStatus').value;

        if (searchText === "" && selectedField === "" && selectedStatus === ""){
            bindingDataTable("/api/offer?pageSize=2&pageIndex=" + id);
        }else {
            bindingDataTable("api/offer/v1?pageSize=2&pageIndex=" + id + "&keyword="
                + searchText + "&department=" + selectedField + "&status=" + selectedStatus);
        }
    })
}
document.getElementById("search-button")
    .addEventListener("click", () => {
        let searchText = document.getElementById("search-input").value;
        let selectedField = document.querySelector('#selectField').value;
        let selectedStatus = document.querySelector('#selectedStatus').value;

        if (searchText == null && selectedField == null && selectedStatus == null){
            bindingDataTable("/api/offer");
        }

        bindingDataTable("api/offer/v1?keyword=" + searchText + "&department=" + selectedField + "&status=" + selectedStatus);

    })


const prevPage = () => {
    const currentPage = parseInt($('.pagination .active').attr('index'));
    const prevPage = parseInt(currentPage) - 1;


    let searchText = document.getElementById("search-input").value;
    let selectedField = document.querySelector('#selectField').value;
    let selectedStatus = document.querySelector('#selectedStatus').value;

    if (searchText === "" && selectedField === "" && selectedStatus === ""){
        bindingDataTable("/api/offer?pageSize=2&pageIndex=" + prevPage);
    }else {
        bindingDataTable("api/offer/v1?pageSize=2&pageIndex=" + prevPage + "keyword="
            + searchText + "&department=" + selectedField + "&status=" + selectedStatus);
    }
};

const nextPage = () => {

    const currentPage = parseInt($('.pagination .active').attr('index'));
    const nextPage = parseInt(currentPage) + 1;

    let searchText = document.getElementById("search-input").value;
    let selectedField = document.querySelector('#selectField').value;
    let selectedStatus = document.querySelector('#selectedStatus').value;

    if (searchText === "" && selectedField === "" && selectedStatus === ""){
        bindingDataTable("/api/offer?pageSize=2&pageIndex=" + nextPage);
    }else {
        bindingDataTable("api/offer/v1?pageSize=2&pageIndex=" + nextPage + "keyword="
            + searchText + "&department=" + selectedField + "&status=" + selectedStatus);
    }
};
bindingDataTable('/api/offer');

//ẩn hiện mập mờ
function showExport() {
    document.getElementById("export-offer").style.display = "block";
}
function hiddenExport() {
    document.getElementById("export-offer").style.display = "none";
}

/*
 function cancelOffer(id) {
     var option = confirm('Are you sure you want to cancel this offer');
     if (option === true) {
         window.location.href = "/offer/cancel-offer/" + id;
     }
 }*/
