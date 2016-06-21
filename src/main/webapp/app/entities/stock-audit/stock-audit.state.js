(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-audit', {
            parent: 'entity',
            url: '/stock-audit?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.stockAudit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-audit/stock-audits.html',
                    controller: 'StockAuditController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockAudit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-audit-detail', {
            parent: 'entity',
            url: '/stock-audit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.stockAudit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-audit/stock-audit-detail.html',
                    controller: 'StockAuditDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockAudit');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockAudit', function($stateParams, StockAudit) {
                    return StockAudit.get({id : $stateParams.id});
                }]
            }
        })
        .state('stock-audit.new', {
            parent: 'stock-audit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-audit/stock-audit-dialog.html',
                    controller: 'StockAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                opDate: null,
                                antValue: null,
                                newValue: null,
                                antShares: null,
                                newShares: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-audit', null, { reload: true });
                }, function() {
                    $state.go('stock-audit');
                });
            }]
        })
        .state('stock-audit.edit', {
            parent: 'stock-audit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-audit/stock-audit-dialog.html',
                    controller: 'StockAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockAudit', function(StockAudit) {
                            return StockAudit.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-audit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-audit.delete', {
            parent: 'stock-audit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-audit/stock-audit-delete-dialog.html',
                    controller: 'StockAuditDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockAudit', function(StockAudit) {
                            return StockAudit.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-audit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
