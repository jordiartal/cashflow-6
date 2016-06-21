(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('SavingsAuditController', SavingsAuditController);

    SavingsAuditController.$inject = ['$scope', '$state', 'SavingsAudit', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants','entity'];

    function SavingsAuditController ($scope, $state, SavingsAudit, ParseLinks, AlertService, pagingParams, paginationConstants, entity) {
        var vm = this;
        vm.loadAll = loadAll;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.loadAll();
        var dadesconsulta = entity;
        $scope.dadesconsulta;
        console.log(dadesconsulta);

        function loadAll () {
            SavingsAudit.query({
                page: pagingParams.page - 1,
                size: paginationConstants.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.savingsAudits = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

    }
})();
