(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('StockAuditDetailController', StockAuditDetailController);

    StockAuditDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'StockAudit', 'Stock'];

    function StockAuditDetailController($scope, $rootScope, $stateParams, entity, StockAudit, Stock) {
        var vm = this;
        vm.stockAudit = entity;
        
        var unsubscribe = $rootScope.$on('cashflow6App:stockAuditUpdate', function(event, result) {
            vm.stockAudit = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
