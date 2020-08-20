function addAndEdit(taskID, action) {
    var x = parseInt(taskID)
    document.getElementById('formaddedit').style.display = 'flex'
    var currentPage = document.getElementById('current-page').value;
    if(action.localeCompare('edit') == 0) {
        $.ajax({
            url: '/tasks?page='+currentPage,
            type: "GET",
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    const element = data[i];
                    if(x === parseInt(element.id)) {
                        document.getElementById("id-task").value = x;
                        document.getElementById("content-task").value = element.content;
                    }
                }
                dataDisplay(data);
            }
        })
    }
}

$(document).ready(function () {
    $(window).scroll(function () {
        if ($(this).scrollTop() > 50) {
            $('#back-to-top').fadeIn();
        } else {
            $('#back-to-top').fadeOut();
        }
    });
    // scroll body to 0px on click
    $('#back-to-top').click(function () {
        $('body,html').animate({
            scrollTop: 0
        }, 400);
        return false;
    });
});

function dataDisplay(data) {
    $('#tableBody').html("");
    document.getElementById("error").innerHTML = "";
    for (var i = 0; i <= data.length; i++) {
        const element = data[i];
        if (element.status == 0) {
            var a = element.id;
            $('#tableBody').append("<tr>" +
                "<th scope=\"row\">" + element.index + "</th>" +
                "<td id='subjectTask'>" + element.content + "</td>" +
                "<td><button type=\"button\" class=\"btn btn-danger\" onclick=taskPassed(" + element.id + ")><i class=\"far fa-frown\"></i></button></td>" +
                "<td><button type=\"button\" class=\"btn btn-info\" onclick=addAndEdit(" + element.id + ",'edit')><i class=\"far fa-edit\"></i></button></td>" +
                " <td><button type='button' class='btn btn-warning' onclick=taskDelete(" + element.id + ")><i class=\"fas fa-trash-alt\"></i></i></button></td>" +
                "</tr>");

        } else if (element.status == 1) {
            $('#tableBody').append("<tr>" +
                "<th scope=\"row\">" + element.index + "</th>" +
                "<td id='subjectTask'>" + element.content + "</td>" +
                "<td><button type=\"button\" class=\"btn btn-success\" onclick=taskFailed(" + element.id + ")><i class=\"far fa-smile\"></i></button></td>" +
                "<td><button type=\"button\" class=\"btn btn-info\" onclick=addAndEdit(" + element.id + ",'edit')><i class=\"far fa-edit\"></i></button></td>" +
                " <td><button type='button' class='btn btn-warning' onclick=taskDelete(" + element.id + ")><i class=\"fas fa-trash-alt\"></i></i></button></td>" +
                "</tr>");
        }
    }
};

function displayLstPage(data) {
    for (var i = 0; i < data.length; i++) {
        const element = data[i];
        $('#page-item-number').append("<button class=\"btn btn-outline-dark rounded mr-2\" onclick=page(" + element + ") id=\"button-page-" + element + "\"><a href=\"#\" class=\"justify-content-center\">" + element + "</a></button>")
    }
}

$(document).ready(function () {
    var currentPage = document.getElementById('current-page').value;
    $.ajax({
        url: 'page?pageInput=' + currentPage,
        type: "GET",
        success: function (data) {
            displayLstPage(data);
            document.getElementById('button-page-prev').style.display = 'none';
            document.getElementById('button-page-1').classList.add("selected");
            $.ajax({
                    url: '/tasks',
                    type: "GET",
                    success: function (data) {
                        dataDisplay(data);
                    }
                }
            )
        }
    })
});


function taskPassed(id) {
    console.log(id);
    var currentPage = document.getElementById('current-page').value;
    $.ajax({
        url: 'taskcrud?id=' + id + '&action=passed&page=' + currentPage,
        type: "GET",
        success: function (data) {
            dataDisplay(data)
        }
    })
}

function taskFailed(id) {
    console.log(id);
    var currentPage = document.getElementById('current-page').value;
    $.ajax({
        url: 'taskcrud?id=' + id + '&action=failed&page=' + currentPage,
        type: "GET",
        success: function (data) {
            dataDisplay(data)
        }
    })
}

function taskDelete(id) {
    var totalPage = document.getElementById('total-page').value;
    console.log(id);
    var currentPage = document.getElementById('current-page').value;
    console.log(currentPage);
    $.ajax({
        url: 'taskcrud?id=' + id + '&action=delete&page=' + currentPage,
        type: "GET",
        success: function () {
            $.ajax({
                url: 'page?pageInput=' + currentPage,
                type: "GET",
                success: function (data) {
                    for(var i = 1; i <= parseInt(totalPage); i++) {
                        $('#button-page-'+i).remove();
                    }
                    displayLstPage(data);
                    document.getElementById('button-page-'+currentPage).classList.add("selected");
                    document.getElementById('current-page').value = currentPage;
                    $.ajax({
                            url: '/tasks?page=' + currentPage,
                            type: "GET",
                            success: function (data) {
                                dataDisplay(data);
                            }
                        }
                    )
                }
            })
        }
    })
}

function serverValidate() {
    var input = document.getElementById("content-task").value;
    var id = document.getElementById("id-task").value;
    var currentPage = document.getElementById('current-page').value;
    $.ajax({
        url: 'taskcrud?id=' + id + '&action=create&page='+ currentPage +'&content=' + input,
        type: "GET",
        success: function (data) {
            document.getElementById("id-task").value = 0;
            dataDisplay(data);
        },
        error: function (err) {
            console.log(err);
            if (err.responseText === 'DATA_EMPTY') {
                document.getElementById("error").innerHTML = "Content of Task Must BE Filled Out";
            } else if (err.responseText === 'LENGTH_ERROR') {
                document.getElementById("error").innerHTML = "Content of Task Must NOT Have Length Greater Than 50 Characters!!!";
            }

        }
    })

}

function page(page) {
    var totalPage = document.getElementById('total-page').value;

    if(parseInt(page) === 1) {
        document.getElementById('button-page-prev').style.display = 'none';
    } else {
        document.getElementById('button-page-prev').style.display = 'block';
    }

    if(parseInt(totalPage) === parseInt(page)) {
        document.getElementById('button-page-next').style.display = 'none';
    } else {
        document.getElementById('button-page-next').style.display = 'block';
    }

    var currentPage = document.getElementById('current-page').value;
    document.getElementById('button-page-' + currentPage).classList.remove("selected");
    $.ajax({
        url: 'page?pageInput=' + page,
        type: "GET",
        success: function (data) {
            for(var i = 1; i <= parseInt(totalPage); i++) {
                $('#button-page-'+i).remove();
            }
            displayLstPage(data);
            document.getElementById('button-page-'+page).classList.add("selected");
            document.getElementById('current-page').value = page;
            $.ajax({
                    url: '/tasks?page='+page,
                    type: "GET",
                    success: function (data) {
                        dataDisplay(data)
                    }
                }
            )
        }
    })



}

function firstButton() {
    page(1);
}

function previousButton() {
    var prePage = parseInt(document.getElementById('current-page').value) - 1;
    if(prePage === 1) {
        firstButton();
    } else {
        page(prePage);
    }
}

function nextButton() {
    var totalPage = parseInt(document.getElementById('total-page').value);
    var nextPage = parseInt(document.getElementById('current-page').value) + 1;
    if(totalPage === nextPage) {
        lastButton();
    } else {
        page(nextPage);
    }
}

function lastButton() {
    var totalPage = document.getElementById('total-page').value;
    page(totalPage);
}
