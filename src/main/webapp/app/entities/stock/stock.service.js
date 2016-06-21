(function() {
    'use strict';
    angular
        .module('cashflow6App')
        .factory('Stock', Stock);

    Stock.$inject = ['$resource', 'DateUtils'];

    function Stock ($resource, DateUtils) {
        var resourceUrl =  'api/stocks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.initialDate = DateUtils.convertLocalDateFromServer(data.initialDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.initialDate = DateUtils.convertLocalDateToServer(data.initialDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.initialDate = DateUtils.convertLocalDateToServer(data.initialDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
