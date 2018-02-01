/*
 * Metadata - jQuery plugin for parsing metadata from elements
 *
 * Copyright (c) 2006 John Resig, Yehuda Katz, J�örn Zaefferer, Paul McLanahan
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Revision: $Id: jquery.metadata.js 3640 2007-10-11 18:34:38Z pmclanahan $
 *
 */

/**
 * Sets the type of metadata to use. Metadata is encoded in JSON, and each property
 * in the JSON will become a property of the element itself.
 *
 * There are four supported types of metadata storage:
 *
 *   attr:  Inside an attribute. The name parameter indicates *which* attribute.
 *
 *   class: Inside the class attribute, wrapped in curly braces: { }
 *
 *   elem:  Inside a child element (e.g. a script tag). The
 *          name parameter indicates *which* element.
 *   html5: Values are stored in data-* attributes.
 *
 * The metadata for an element is loaded the first time the element is accessed via jQuery.
 *
 * As a result, you can define the metadata type, use $(expr) to load the metadata into the elements
 * matched by expr, then redefine the metadata type and run another $(expr) for other elements.
 *
 * @name $.metadata.setType
 *
 * @example <p id="one" class="some_class {item_id: 1, item_label: 'Label'}">This is a p</p>
 * @before $.metadata.setType("class")
 * @after $("#one").metadata().item_id == 1; $("#one").metadata().item_label == "Label"
 * @desc Reads metadata from the class attribute
 *
 * @example <p id="one" class="some_class" data="{item_id: 1, item_label: 'Label'}">This is a p</p>
 * @before $.metadata.setType("attr", "data")
 * @after $("#one").metadata().item_id == 1; $("#one").metadata().item_label == "Label"
 * @desc Reads metadata from a "data" attribute
 *
 * @example <p id="one" class="some_class"><script>{item_id: 1, item_label: 'Label'}</script>This is a p</p>
 * @before $.metadata.setType("elem", "script")
 * @after $("#one").metadata().item_id == 1; $("#one").metadata().item_label == "Label"
 * @desc Reads metadata from a nested script element
 *
 * @example <p id="one" class="some_class" data-item_id="1" data-item_label="Label">This is a p</p>
 * @before $.metadata.setType("html5")
 * @after $("#one").metadata().item_id == 1; $("#one").metadata().item_label == "Label"
 * @desc Reads metadata from a series of data-* attributes
 *
 * @param String type The encoding type
 * @param String name The name of the attribute to be used to get metadata (optional)
 * @cat Plugins/Metadata
 * @descr Sets the type of encoding to be used when loading metadata for the first time
 * @type undefined
 * @see metadata()
 */

(function($) {

    $.extend({
        metadata : {
            defaults : {
                type: 'class',
                name: 'metadata',
                cre: /({.*})/,
                single: 'metadata'
            },
            setType: function( type, name ){
                this.defaults.type = type;
                this.defaults.name = name;
            },
            get: function( elem, opts ){
                var settings = $.extend({},this.defaults,opts);
                // check for empty string in single property
                if ( !settings.single.length ) settings.single = 'metadata';

                var data = $.data(elem, settings.single);
                // returned cached data if it already exists
                if ( data ) return data;

                data = "{}";

                var getData = function(data) {
                    if(typeof data != "string") return data;

                    if( data.indexOf('{') < 0 ) {
                        data = eval("(" + data + ")");
                    }
                }

                var getObject = function(data) {
                    if(typeof data != "string") return data;

                    data = eval("(" + data + ")");
                    return data;
                }

                if ( settings.type == "html5" ) {
                    var object = {};
                    $( elem.attributes ).each(function() {
                        var name = this.nodeName;
                        if(name.match(/^data-/)) name = name.replace(/^data-/, '');
                        else return true;
                        object[name] = getObject(this.nodeValue);
                    });
                } else {
                    if ( settings.type == "class" ) {
                        var m = settings.cre.exec( elem.className );
                        if ( m )
                            data = m[1];
                    } else if ( settings.type == "elem" ) {
                        if( !elem.getElementsByTagName ) return;
                        var e = elem.getElementsByTagName(settings.name);
                        if ( e.length )
                            data = $.trim(e[0].innerHTML);
                    } else if ( elem.getAttribute != undefined ) {
                        var attr = elem.getAttribute( settings.name );
                        if ( attr )
                            data = attr;
                    }
                    object = getObject(data.indexOf("{") < 0 ? "{" + data + "}" : data);
                }

                $.data( elem, settings.single, object );
                return object;
            }
        }
    });

    /**
     * Returns the metadata object for the first member of the jQuery object.
     *
     * @name metadata
     * @descr Returns element's metadata object
     * @param Object opts An object contianing settings to override the defaults
     * @type jQuery
     * @cat Plugins/Metadata
     */
    $.fn.metadata = function( opts ){
        return $.metadata.get( this[0], opts );
    };

})(jQuery);
Raw
jquery.tableFilterable.js
/* jquery.tablefilterable v1.0 */

/*
options = {
pageableSelector: String (css selector),
sortableSelector: String (css selector),
filterRowCssClass: String (defaults to 'filter-row'),
triggerEvent: Function
};
*/

(function (jQuery) {

    // Adds a case insensitive :insensitiveContains selector
    jQuery.expr[":"].insensitiveContains = function (a, i, m) {
        return (a.textContent || a.innerText || "").toLowerCase().indexOf(m[3].toLowerCase()) >= 0;
    };

    jQuery.fn.tableFilterable = function (options) {

        var tables = jQuery(this);
        var defaults = {
            pageableSelector: "table.pageable",
            sortableSelector: "table.sortable",
            // This is the event that triggers the filtering, 
            // for larger tables, setting this to 'change' might be more performant
            triggerEvent: "keyup",
            filterRowCssClass: "filter-row"
        };
        var opts = jQuery.extend(defaults, options);

        tables.each(function () {

            var el = this;

            // If filtering is already enabled, the continue to next table
            if (el.filterable) { return; }
            el.filterable = true;

            // Add filter row
            var trFilterRow = jQuery("<tr class='" + opts.filterRowCssClass + "'></tr>");
            jQuery("thead tr:first-child th", el).each(function () {
                var cell = jQuery("<th></th>");
                if (!jQuery(this).is(".no-filter")) {
                    cell.append("<input type='text' />");
                }
                if (!jQuery(this).is(":visible")) { // Prevent hidden columns from showing in the filter row
                    cell.css("display", "none");
                }
                trFilterRow.append(cell);
            });
            jQuery("thead", el).append(trFilterRow);

            jQuery("thead tr:last-child th :input", el).bind(opts.triggerEvent, function () {
                el.updateFilter();
            });

            el.getFilterValues = function () {
                var values = [];
                jQuery("thead tr:last-child th", this).each(function (i, cell) {
                    var input = jQuery(":input", cell).get(0);
                    var val = "";
                    if (input) { val = jQuery(input).val(); }
                    values.push(val);
                });
                return values;
            };

            el.updateFilter = function () {
                var values = this.getFilterValues();
                jQuery("tbody tr", this).each(function () {
                    var cells = jQuery("td", this);
                    var hideRow = false;
                    for (var i = 0; i < values.length; i++) {
                        if (values[i].length > 0) {
                            var cell = jQuery(cells[i]);
                            var meta = cell.metadata().filterValue;
                            if (meta) {
                                if (meta.toLowerCase().indexOf(values[i].toLowerCase()) == -1) { hideRow = true; }
                            } else {
                                if (!cell.is(":insensitiveContains('" + values[i] + "')")) {
                                    hideRow = true;
                                }
                            }
                        }
                    }
                    if (hideRow) {
                        jQuery(this).addClass("filtered-out").hide();
                    } else {
                        jQuery(this).removeClass("filtered-out").show();
                    }
                });
                if (!this.isFiltered()) {
                    jQuery(el).trigger("unfiltered");
                } else {
                    jQuery(el).trigger("filtered");
                }
            };

            el.isFiltered = function () {
                var isFiltered = false;
                var values = this.getFilterValues();
                for (var i = 0; i < values.length; i++) {
                    if (values[i] != "") { isFiltered = true; }
                }
                return isFiltered;
            };
        });
    };
})(jQuery);
Raw
jquery.tablePageable.js
/* jquery.tablePageable v1.1 */

/*
options = {
sizeSelect: Element,
pageSize: Number,
onPageChange: Callback,
onPageSizeChange: Callback
sortableSelector: String (css selector)
filterableSelector: String (css selector)
containerClass: String (css class)
};
*/

(function (jQuery) {
    jQuery.fn.tablePageable = function (options) {

        var tbl = this;
        var defaults = {
            pageSize: 10,
            sortableSelector: "table.sortable",
            filterableSelector: "table.filterable",
            containerClass: "pageable-container"
        };
        var opts = jQuery.extend(defaults, options);

        tbl.each(function () {

            var el = this;

            // If paging is already enabled, the continue to next table
            if (el.pageable) { return; }
            el.pageable = true;

            el.pageSize = opts.pageSize;
            el.pageIndex = 0;

            el.getRows = function () { return jQuery(this).find("tbody tr"); };

            el.getPageCount = function () {
                var rowCount = el.getRows().length;
                return (rowCount % this.pageSize) ? Math.floor(rowCount / this.pageSize) + 1 :
                    (rowCount / this.pageSize);
            };

            el.changePage = function (toIndex) {
                var pageCount = this.getPageCount();
                if (toIndex >= 0 && toIndex <= (pageCount - 1)) {
                    this.pageIndex = toIndex;
                    this.updatePaging();
                    this.updateIndicator();
                    if (opts.onPageChange) {
                        opts.onPageChange.call(pageIndex);
                    }
                }
            };

            el.changePageSize = function (toSize) {
                this.pageSize = toSize;
                this.updatePaging();
                this.updateIndicator();
                if (opts.onPageSizeChange) {
                    opts.onPageSizeChange.call(pageSize);
                }
            };

            el.updateIndicator = function () {
                jQuery(this).closest("div." + opts.containerClass).find("input.pagedisplay")
                    .val((this.pageIndex + 1) + "/" + this.getPageCount());
            };

            el.updatePaging = function () {
                var start = this.pageIndex * this.pageSize;
                var end = start + this.pageSize;
                var rows = this.getRows();
                jQuery(rows).hide();
                for (var i = start; i < end && i < rows.length; i++) {
                    jQuery(rows[i]).show();
                }
                jQuery(el).trigger("paged");

                // If the jQuery.tableFilterable plugin is being used then
                // re-hide any items that have been filtered out
                if (jQuery(el).is(opts.filterableSelector)) {
                    jQuery("tr.filtered-out", el).hide();
                }
            };

            // If jQuery.tableFilterable plugin is being used then handle
            // the 'filtered' and 'unfiltered' events
            if (jQuery(el).is(opts.filterableSelector)) {
                jQuery(el).bind("unfiltered", function () {
                    el.updatePaging();
                    jQuery(el).closest("div." + opts.containerClass).find("div.pager-container").show();
                });
                jQuery(el).bind("filtered", function () {
                    jQuery(el).closest("div." + opts.containerClass).find("div.pager-container").hide();
                });
            }

            createPagerControls(jQuery(el));
            bindControlHandlers(jQuery(el));
            bindSortEvents(jQuery(el));
            el.updatePaging();
            el.updateIndicator();

        });

        function createPagerControls(table) {

            var pagerHtml = jQuery("<div class='pager-container pager clearfix'>" +
                "<span class='first'>First</span>" +
                "<span class='prev'>Prev</span>" +
                "<input type='text' class='pagedisplay' size='4' disabled='disabled' />" +
                "<span class='next'>Next</span>" +
                "<span class='last'>Last</span>" +
                "<select class='pagesize'>" +
                "<option selected='selected' value='10'>10</option>" +
                "<option value='20'>20</option>" +
                "<option value='40'>40</option>" +
                "<option value='60'>60</option>" +
                "<option  value='100'>100</option>" +
                "</select>" +
                "<span class='count'>Count: " + jQuery("tbody tr", table).length + "</span>" +
                "</div>");

            var outerDiv = jQuery("<div class='" + opts.containerClass + "'></div>");

            table.before(outerDiv);
            outerDiv.append(table);
            outerDiv.append(pagerHtml);
        }

        function bindControlHandlers(table) {

            var div = table.parent();
            var el = table[0];

            div.find("span.first").bind("click", function () { el.changePage(0); });
            div.find("span.prev").bind("click", function () { el.changePage(el.pageIndex - 1); });
            div.find("span.next").bind("click", function () { el.changePage(el.pageIndex + 1); });
            div.find("span.last").bind("click", function () { el.changePage(el.getPageCount() - 1); });
            div.find("select.pagesize").bind("change", function () { el.changePageSize(parseInt(jQuery(this).val())); });
        }

        function bindSortEvents(table) {
            var el = jQuery(table[0]);
            if (el.is(opts.sortableSelector)) {
                el.bind("sorted", function () {
                    // If jQuery.tableFilterable plugin is being used, check first
                    // if the grid is filtered and if so, don't update the paging
                    if (el.is(opts.filterableSelector)) {
                        if (!this.isFiltered()) {
                            this.updatePaging();
                        }
                    } else {
                        this.updatePaging();
                    }
                });
            }
        }
    };
})(jQuery);
Raw
jquery.tableSortable.js
/* jquery.tableSortable v1.0 */

/*
options = {
pageableSelector: String (css selector),
filterableSelector: String (css selector),
maintainState: Boolean (defaults to true and relies on session.js)
};
*/

(function (jQuery) {
    jQuery.fn.tableSortable = function (options) {

        var tables = this;
        var defaults = {
            pageableSelector: "table.pageable",
            filterableSelector: "table.filterable",
            maintainState: true
        };
        var opts = jQuery.extend(defaults, options);

        var sorts = {}; sorts["up"] = [1, -1]; sorts["down"] = [-1, 1];
        var types = {};
        types["alpha"] = function (x) { return x.toString(); };
        types["number"] = function (x) { return parseInt(x); };
        types["date"] = function (x) { return new Date(x); };

        function getConverter(col) {
            for (var t in types) {
                if (col.metadata().dataType == t) { return types[t]; }
            }
            return types["alpha"];
        }

        tables.each(function () {

            var el = this;

            // If sorting is already enabled, the continue to next table
            if (el.sortable) { return; }
            el.sortable = true;

            $("thead tr:first-child th", el).each(function () {
                $(this).prepend("<div class=\"sort-icon\"></div>");
            });

            el.getColumn = function (idx) { return jQuery(jQuery(this).find("thead tr").children()[idx]); };

            el.sort = function (idx, dir) {

                var col = this.getColumn(idx);
                var rows = jQuery(this).find("tbody > tr").get();

                rows.sort(function (a, b) {

                    var tdA = jQuery(a).children("td").eq(idx), tdB = jQuery(b).children("td").eq(idx);
                    var metaA = tdA.metadata().sortValue, metaB = tdB.metadata().sortValue;
                    var valA = "", valB = "";

                    if (metaA && metaB) {
                        valA = metaA.toLowerCase();
                        valB = metaB.toLowerCase();
                    } else {
                        valA = tdA.text().toLowerCase();
                        valB = tdB.text().toLowerCase();
                    }

                    var keyA = getConverter(col)(valA);
                    var keyB = getConverter(col)(valB);
                    if (keyA < keyB) return sorts[dir][0];
                    if (keyA > keyB) return sorts[dir][1];
                    return 0;
                });

                jQuery.each(rows, function (i, row) {
                    jQuery(el).children("tbody").append(row);
                });

                // If the jQuery.tableFilterable plugin is being used then
                // re-hide any items that have been filtered out
                if (jQuery(el).is(opts.filterableSelector)) {
                    jQuery("tr.filtered-out", el).hide();
                }

                jQuery("th", this).removeClass("sort-up").removeClass("sort-down").each(function () {
                    if (jQuery(this).attr("class") == "") { jQuery(this).removeAttr("class"); }
                });
                col.addClass("sort-" + dir);

                jQuery(this).trigger("sorted");
            };

            jQuery("th", this).each(function (idx) {
                var th = jQuery(this);
                var tbl = th.closest("table");
                var sesName = window.location + tbl.attr("id");
                if (!th.is(".no-sort")) {
                    th.toggle(
                        function () {
                            el.sort(idx, "up");
                        },
                        function () {
                            el.sort(idx, "down");
                        });
                }
            });
        });
    };
})(jQuery);