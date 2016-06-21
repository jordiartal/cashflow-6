(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('SavingsAuditDetailController', SavingsAuditDetailController);

    SavingsAuditDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SavingsAudit', 'Savings'];

    function SavingsAuditDetailController($scope, $rootScope, $stateParams, entity, SavingsAudit, Savings) {
        var vm = this;
        vm.savingsAudit = entity;
        
        var unsubscribe = $rootScope.$on('cashflow6App:savingsAuditUpdate', function(event, result) {
            vm.savingsAudit = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
