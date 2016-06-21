(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('currency', {
            parent: 'entity',
            url: '/currency?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.currency.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/currency/currencies.html',
                    controller: 'CurrencyController',
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
                    $translatePartialLoader.addPart('currency');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('currency-detail', {
            parent: 'entity',
            url: '/currency/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.currency.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/currency/currency-detail.html',
                    controller: 'CurrencyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('currency');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Currency', function($stateParams, Currency) {
                    return Currency.get({id : $stateParams.id});
                }]
            }
        })
        .state('currency.new', {
            parent: 'currency',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/currency/currency-dialog.html',
                    controller: 'CurrencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                code: null,
                                conver: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('currency', null, { reload: true });
                }, function() {
                    $state.go('currency');
                });
            }]
        })
        .state('currency.edit', {
            parent: 'currency',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/currency/currency-dialog.html',
                    controller: 'CurrencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Currency', function(Currency) {
                            return Currency.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('currency', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('currency.delete', {
            parent: 'currency',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/currency/currency-delete-dialog.html',
                    controller: 'CurrencyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Currency', function(Currency) {
                            return Currency.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('currency', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
