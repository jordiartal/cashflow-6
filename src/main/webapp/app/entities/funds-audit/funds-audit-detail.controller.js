(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('FundsAuditDetailController', FundsAuditDetailController);

    FundsAuditDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'FundsAudit', 'Funds'];

    function FundsAuditDetailController($scope, $rootScope, $stateParams, entity, FundsAudit, Funds) {
        var vm = this;
        vm.fundsAudit = entity;
        
        var unsubscribe = $rootScope.$on('cashflow6App:fundsAuditUpdate', function(event, result) {
            vm.fundsAudit = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
