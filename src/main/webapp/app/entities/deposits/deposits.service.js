(function() {
    'use strict';
    angular
        .module('cashflow6App')
        .factory('Deposits', Deposits);

    Deposits.$inject = ['$resource', 'DateUtils'];

    function Deposits ($resource, DateUtils) {
        var resourceUrl =  'api/deposits/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.initialDate = DateUtils.convertLocalDateFromServer(data.initialDate);
                        data.expDate = DateUtils.convertLocalDateFromServer(data.expDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.initialDate = DateUtils.convertLocalDateToServer(data.initialDate);
                    data.expDate = DateUtils.convertLocalDateToServer(data.expDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.initialDate = DateUtils.convertLocalDateToServer(data.initialDate);
                    data.expDate = DateUtils.convertLocalDateToServer(data.expDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
