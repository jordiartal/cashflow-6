(function() {
    'use strict';
    angular
        .module('cashflow6App')
        .factory('UserData', UserData);

    UserData.$inject = ['$resource', 'DateUtils'];

    function UserData ($resource, DateUtils) {
        var resourceUrl =  'api/user-data/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.birthday = DateUtils.convertLocalDateFromServer(data.birthday);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.birthday = DateUtils.convertLocalDateToServer(data.birthday);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.birthday = DateUtils.convertLocalDateToServer(data.birthday);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
