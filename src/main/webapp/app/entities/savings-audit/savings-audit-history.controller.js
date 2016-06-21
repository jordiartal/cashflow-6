
        'use strict';

        angular
            .module('cashflow6App')
            .controller('SavingsAuditHistoryController', SavingsAuditController);

        SavingsAuditController.$inject = ['$scope', 'entity'];

        function SavingsAuditController ($scope, entity) {

            $scope.entidad = entity;
            console.log (entity);


    };
    
