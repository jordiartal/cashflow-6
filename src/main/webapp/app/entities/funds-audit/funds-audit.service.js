(function() {
    'use strict';
    angular
        .module('cashflow6App')
        .factory('FundsAudit', FundsAudit);

    FundsAudit.$inject = ['$resource', 'DateUtils'];

    function FundsAudit ($resource, DateUtils) {
        var resourceUrl =  'api/funds-audits/:id';

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
            },
            'history': {
                method: 'GET',
                isArray : true,
                url: 'api/funds/findAudit/:id',
                interceptor: {
                    response: function(response) {
                        response.resource.$httpHeaders = response.headers;
                        return response.resource;
                    }
                }
            }
        });
    }
})();
