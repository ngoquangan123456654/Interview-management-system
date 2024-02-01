const apiUrl = "/schedule/api/v1"
var currentPage = 0;
var totalPage = 0;
var totalElements = 0;
var numberOfElements = 0;

function start() {

    getData(apiUrl, renderUI);

}

start();

// Function handle data
function getData(url, callback) {
    fetch(url, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
        },
    })
        .then(response => response.json())
        .then(callback);
}

function renderUI(pageData) {
    // console.log(pageData)
    currentPage = pageData.pageable.pageNumber;
    // console.log(currentPage)
    totalPage = pageData.totalPages;
    totalElements = pageData.totalElements;
    numberOfElements = pageData.numberOfElements;

    $('#schedule-list').empty();

    if (pageData.content.length === 0) {
        $('#schedule-table').css({display:"none"});
        $('.schedule-empty-text').css({display:"block"});
    } else {
        $('#schedule-table').css({display:"table"});
        $('.schedule-empty-text').css({display:"none"});
    }

    pageData.content.forEach(e => {

        $('#schedule-list').append(`
            <tr>
                <td>${e.title}</td>
                <td>${e.candidate != null ? e.candidate.fullName : "N/A"}</td>
                <td>${e.mainInterviewer != "null" ? e.mainInterviewer : "N/A"}</td>
                <td>${e.scheduleDate + ' ' + e.scheduleTimeFrom + '-' + e.scheduleTimeTo}</td>
                <td>${e.result}</td>
                <td>${e.status}</td>
                <td>${e.job}</td>
                <td class="d-flex justify-content-between" style="font-size: 0.95rem;">
                    <a href="/schedule/info/${e.scheduleId}" sec:authorize="hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN', 'INTERVIEWER')"><i class="mdi mdi-eye"></i></a>
                    <a href="/schedule/update/${e.scheduleId}" sec:authorize="hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')"><i class="mdi mdi-lead-pencil"></i></a>
                    <a href="/offer/add/${e.scheduleId}" sec:authorize="hasAnyRole('RECRUITER', 'ADMIN')"><i class="mdi mdi-arrange-send-backward"></i></a>
                </td>
            </tr>
        `)
    })

    $('#schedule-paging-num').empty();
    for (let i = 0; i < pageData.totalPages; i++) {
        let active = i === currentPage ? "active" : "";

        $('#schedule-paging-num').append(`
            <a href="#" class="page-link ${active}" page="${i}"  onclick="pagingHandleClick(event,this)">
                ${i + 1}
            </a>
        `)
    }

    //Display pagination next and previous button
    if (totalPage <= 1) {
        $('.pagination-btn-wrap').css({display:"none"});
    }
    else {
        $('.pagination-btn-wrap').css({display:"flex"});
    }

    if ((currentPage > 0) && (currentPage < totalPage - 1)) {
        $('#prev-btn').css({display: "block"});
        $('#next-btn').css({display: "block"});

    } else if (currentPage >= totalPage - 1) {
        console.log("You are on the last page");
        $('#next-btn').css({display: "none"});
        $('#prev-btn').css({display: "block"});
    } else {
        console.log("You are on the first page");
        $('#next-btn').css({display: "block"});
        $('#prev-btn').css({display: "none"});
    }

    updatePaginationInfo();
}


//Define function
function pagingHandleClick(event, element) {
    event.preventDefault();
    let targetElement = event.target;

    let searchText = document.getElementById("search-text").value;
    let searchInterviewer = document.getElementById("search-interviewer").value;
    let searchStatus = document.getElementById("search-status").value;
    let searchQuantity = document.getElementById("search-quantity").value;

    let pagingUrl = `/schedule/api/v1?pageSize=${searchQuantity}`;
    if (searchText !== '') {
        pagingUrl += `&keyword=${searchText}`;
    }
    if (searchInterviewer !== '') {
        pagingUrl += `&interviewer=${searchInterviewer}`;
    }
    if (searchStatus !== '') {
        pagingUrl += `&status=${searchStatus}`;
    }

    pagingUrl += `&pageIndex=${targetElement.getAttribute("page")}`;

    getData(pagingUrl, renderUI);
}

function updatePaginationInfo() {
    $('#zero_config_info').text(`Showing ${numberOfElements} of ${totalElements} schedules`);
}

//Handle click next and previous button
$('#next-btn').click(function () {
    currentPage++;

    let pagingUrl = "/schedule/api/v1?pageIndex=" + currentPage;

    let searchText = document.getElementById("search-text").value;
    let searchInterviewer = document.getElementById("search-interviewer").value;
    let searchStatus = document.getElementById("search-status").value;
    let searchQuantity = document.getElementById("search-quantity").value;

    pagingUrl += `&pageSize=${searchQuantity}`;
    if (searchText !== '') {
        pagingUrl += `&keyword=${searchText}`;
    }
    if (searchInterviewer !== '') {
        pagingUrl += `&interviewer=${searchInterviewer}`;
    }
    if (searchStatus !== '') {
        pagingUrl += `&status=${searchStatus}`;
    }

    getData(pagingUrl, renderUI);
});
$('#prev-btn').click(function () {
    currentPage--;

    let pagingUrl = "/schedule/api/v1?pageIndex=" + currentPage;

    let searchText = document.getElementById("search-text").value;
    let searchInterviewer = document.getElementById("search-interviewer").value;
    let searchStatus = document.getElementById("search-status").value;
    let searchQuantity = document.getElementById("search-quantity").value;

    pagingUrl += `&pageSize=${searchQuantity}`;
    if (searchText !== '') {
        pagingUrl += `&keyword=${searchText}`;
    }
    if (searchInterviewer !== '') {
        pagingUrl += `&interviewer=${searchInterviewer}`;
    }
    if (searchStatus !== '') {
        pagingUrl += `&status=${searchStatus}`;
    }

    getData(pagingUrl, renderUI);

});


//Handle submit search
$('#search-btn').click(function (event) {
    event.preventDefault()
    let searchText = document.getElementById("search-text").value;
    let searchInterviewer = document.getElementById("search-interviewer").value;
    let searchStatus = document.getElementById("search-status").value;
    let searchQuantity = document.getElementById("search-quantity").value;

    let pagingUrl = `/schedule/api/v1?pageIndex=0&pageSize=${searchQuantity}`;

    if (searchText !== '') {
        pagingUrl += `&keyword=${searchText}`;
    }
    if (searchInterviewer !== '') {
        pagingUrl += `&interviewer=${searchInterviewer}`;
    }
    if (searchStatus !== '') {
        pagingUrl += `&status=${searchStatus}`;
    }
    getData(pagingUrl, renderUI);

})


function cancelSchedule(id) {
    var option = confirm('Are you sure you want to cancel this schedule');
    if (option === true) {
        window.location.href = "/schedule/" + id + "/cancel";
    }
}


document.getElementById('assign-me').addEventListener("click", function (ev) {
    ev.preventDefault();
    var assignedRecruiter = this.getAttribute('data');
    var recruiterSelect = document.getElementById('recruiter');
    var recruiterValue = document.getElementById('select2-recruiter-container');
    recruiterValue.innerText = assignedRecruiter;
    var options = recruiterSelect.options;
    for (var i = 0; i < options.length; i++) {
        if (options[i].value === assignedRecruiter) {
            options[i].selected = true;
            recruiterSelect.value = options[i].value;
            break;
        }
    }
});