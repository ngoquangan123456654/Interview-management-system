var currentPage = 0;
var totalPage = 0;
var totalElements = 0;
var numberOfElements = 0;
const bindingDataTable = (url) => {
    $.ajax({
        url: url,
        method: "GET",
        dataType: "json"
    }).done((response) => {
        let tableRows = " ";
        if (response.content.length > 0) {
            totalPage = response.totalPages;
            totalElements = response.totalElements;
            numberOfElements = response.numberOfElements;

            response.content.forEach((jobDto) => {
                let skills = jobDto.skill.reduce( (x,y) => x+" "+y ,"").trim().split(" ").join(", ");
                let level = jobDto.level.reduce( (x,y) => x+" "+y ,"").trim().split(" ").join(", ");
                let startDate = formatDDMMYYYY(jobDto.startDate);
                let endDate = formatDDMMYYYY(jobDto.endDate);

                tableRows += `
                            <tr>
                              <td>${jobDto.title}</td>
                              <td>${skills}</td>
                              <td>${startDate}</td>
                              <td>${endDate}</td>
                              <td>${level}</td>
                              <td>${jobDto.status}</td>
                              <td class="d-flex justify-content-between" style="font-size: 0.95rem;">
                                <a href="/job/viewDetail/${jobDto.jobId}">
                                  <i class="mdi mdi-eye"></i></a>
                                <a href="/job/update/${jobDto.jobId}">
                                  <i class="mdi mdi-lead-pencil"></i>
                                </a>
                                <a class="text-danger" href="#" onclick="showMessage(${jobDto.jobId})">
                                 <i class="mdi mdi-delete"></i>
                                </a>
                              </td>
                            </tr>`;
            });
            $('tbody').html(tableRows);
        } else {
            $('tbody').html('<tr><td colspan="7">No item matches with your search data. Please try again.</td></tr>');
        }

        updatePaginationInfo();
    })
        .fail((xhr, status, error) => {
            console.error(`AJAX request failed. Status: ${status}, Error: ${error}`);
        })
        .always(() => {

        })
}

function formatDDMMYYYY(dateStringFromSQL) {
    var dateFromSQL = new Date(dateStringFromSQL);

    // Lấy thông tin ngày, tháng, năm
    var day = dateFromSQL.getDate();
    var month = dateFromSQL.getMonth() + 1; // Tháng bắt đầu từ 0 nên cần cộng thêm 1
    var year = dateFromSQL.getFullYear();

    // Định dạng ngày, tháng, năm thành chuỗi "dd/mm/yyyy"
    return (day < 10 ? '0' : '') + day + '/' + (month < 10 ? '0' : '') + month + '/' + year;
}

document.getElementById("searchButton").addEventListener("click", function (event) {
    event.preventDefault()
    var searchText = document.getElementById("formSearch").value;
    var selectElement = document.getElementById("mySelect").value;
    var pageSize = document.getElementById("selectPageSize").value;

    if (searchText !== '' && selectElement === 'Select') {
        bindingDataTable(`/job/api/v1?keyword=${searchText}&pageSize=${pageSize}`);
    } else if (searchText === '' && selectElement !== 'Select') {
        bindingDataTable(`/job/api/v1?status=${selectElement}&pageSize=${pageSize}`);
    } else if (searchText !== '' && selectElement !== 'Select') {
        bindingDataTable(`/job/api/v1?&keyword=${searchText}&status=${selectElement}&pageSize=${pageSize}`);
    } else {
        bindingDataTable(`/job/api/v1?pageSize=${pageSize}`);
    }
});

function updatePaginationInfo() {
    $('#zero_config_info').text(`${numberOfElements} / ${totalElements} rows`);
}

$('#nextBtn').click(function () {

    if (currentPage < totalPage - 1) {
        var keyword = document.getElementById("formSearch").value;
        var element = document.getElementById("mySelect").value;
        var pageSize = document.getElementById("selectPageSize").value;

        if (keyword !== '' && element === 'Select') {
            currentPage++;
            bindingDataTable(`/job/api/v1?keyword=${keyword}&pageIndex=${currentPage}&pageSize=${pageSize}`);
        } else if (keyword === '' && element !== 'Select') {
            currentPage++;
            bindingDataTable(`/job/api/v1?status=${element}&pageIndex=${currentPage}&pageSize=${pageSize}`);
        } else if (keyword !== '' && element !== 'Select') {
            currentPage++;
            bindingDataTable(`/job/api/v1?keyword=${keyword}&status=${element}&pageIndex=${currentPage}&pageSize=${pageSize}`);
        } else {
            currentPage++;
            bindingDataTable(`/job/api/v1?pageIndex=${currentPage}&pageSize=${pageSize}`);
        }
        $('#prevBtn').prop('disable', false);
    } else {
        console.log("You are on the last page");
        $('#nextBtn').prop('disabled', true);
    }
});

$('#prevBtn').click(function () {
    if (currentPage > 0) {
        var keyword = document.getElementById("formSearch").value;
        var element = document.getElementById("mySelect").value;
        var pageSize = document.getElementById("selectPageSize").value;

        if (keyword !== '' && element === 'Select') {
            currentPage--;
            bindingDataTable(`/job/api/v1?keyword=${keyword}&pageIndex=${currentPage}&pageSize=${pageSize}`);
        } else if (keyword === '' && element !== 'Select') {
            currentPage--;
            bindingDataTable(`/job/api/v1?status=${element}&pageIndex=${currentPage}&pageSize=${pageSize}&`);
        } else if (keyword !== '' && element !== 'Select') {
            currentPage--;
            bindingDataTable(`/job/api/v1?keyword=${keyword}&status=${element}&pageIndex=${currentPage}&pageSize=${pageSize}`);
        } else {
            currentPage--;
            bindingDataTable(`/job/api/v1?&pageIndex=${currentPage}&pageSize=${pageSize}`);
        }

        $('#nextBtn').prop('disabled', false);
    } else {
        console.log("You are on the first page");
        $('#prevBtn').prop('disabled', true);
    }
});

bindingDataTable("/job/api/v1");

function showMessage(id) {
    var option = confirm('Are you sure you want to delete this job ?');
    if(option === true){
        window.location.href = "/job/delete/"+ id;
    }
}