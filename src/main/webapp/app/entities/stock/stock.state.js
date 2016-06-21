(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock', {
            parent: 'entity',
            url: '/stock?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.stock.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock/stocks.html',
                    controller: 'StockController',
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
                    $translatePartialLoader.addPart('stock');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-detail', {
            parent: 'entity',
            url: '/stock/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.stock.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock/stock-detail.html',
                    controller: 'StockDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stock');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Stock', function($stateParams, Stock) {
                    return Stock.get({id : $stateParams.id});
                }]
            }
        })
        .state('stock.new', {
            parent: 'stock',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock/stock-dialog.html',
                    controller: 'StockDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ticker: null,
                                companyName: null,
                                actualShares: null,
                                newShares: null,
                                initialDate: null,
                                actualValue: null,
                                initialValue: null,
                                newValue: null,
                                initialShares: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock', null, { reload: true });
                }, function() {
                    $state.go('stock');
                });
            }]
        })
        .state('stock.edit', {
            parent: 'stock',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock/stock-dialog.html',
                    controller: 'StockDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Stock', function(Stock) {
                            return Stock.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock.delete', {
            parent: 'stock',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock/stock-delete-dialog.html',
                    controller: 'StockDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Stock', function(Stock) {
                            return Stock.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
