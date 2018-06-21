$.get("/market/products", function (products) {
    var template =
        "{{#.}}<div class=\"col-sm-4 col-lg-4 col-md-4\">" +
        "<div class=\"thumbnail\">" +
        "<img alt=\"\" src=\"http://placehold.it/320x150\">" +
        "<div class=\"caption\">" +
        "<h4 class=\"pull-right\">{{price}}</h4>" +
        "<h4><a href=\"/market/view/product/{{id}}\">{{title}}</a></h4>" +
        "<p>{{description}}</p>" +
        "</div>" +
        "<div class=\"ratings\">" +
        "<p class=\"pull-right\">15 reviews</p>" +
        "<p>" +
        "<span class=\"glyphicon glyphicon-star\"></span>" +
        "<span class=\"glyphicon glyphicon-star\"></span>" +
        "<span class=\"glyphicon glyphicon-star\"></span>" +
        "<span class=\"glyphicon glyphicon-star\"></span>" +
        "<span class=\"glyphicon glyphicon-star\"></span>" +
        "</p>" +
        "</div>" +
        "</div>" +
        "</div>{{/.}}";

    $(document).ready(function () {
        $("#products").html(Mustache.to_html(template, products));
    });
});

$.get("/market/products/chart", function (products) {
    var template =
        "{{#.}}<a href=\"/market/view/product/{{id}}\"><div class=\"col-md-6\">" +
        "  <div class=\"thumbnail\">" +
        "    <img src=\"http://placehold.it/320x150\" alt=\"\">" +
        "    <div class=\"caption\">" +
        "      <h4>{{title}}" +
        "      </h4>" +
        "    </div>" +
        "  </div>" +
        "</div></a>{{/.}}";

    if (products.length === 0) {
        document.getElementById('history_label').hidden = true
    }
    $(document).ready(function () {
        $("#history").html(Mustache.to_html(template, products));
    });
});