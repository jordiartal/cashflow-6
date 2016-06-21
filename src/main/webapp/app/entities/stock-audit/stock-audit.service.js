(function() {
    'use strict';
    angular
        .module('cashflow6App')
        .factory('StockAudit', StockAudit);

    StockAudit.$inject = ['$resource', 'DateUtils'];

    function StockAudit ($resource, DateUtils) {
        var resourceUrl =  'api/stock-audits/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.opDate = DateUtils.convertLocalDateFromServer(data.opDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.opDate = DateUtils.convertLocalDateToServer(data.opDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.opDate = DateUtils.convertLocalDateToServer(data.opDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
