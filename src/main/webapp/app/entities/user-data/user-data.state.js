(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-data', {
            parent: 'entity',
            url: '/user-data?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.userData.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-data/user-data.html',
                    controller: 'UserDataController',
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
                    $translatePartialLoader.addPart('userData');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-data-detail', {
            parent: 'entity',
            url: '/user-data/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.userData.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-data/user-data-detail.html',
                    controller: 'UserDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userData');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserData', function($stateParams, UserData) {
                    return UserData.get({id : $stateParams.id});
                }]
            }
        })
        .state('user-data.new', {
            parent: 'user-data',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-data/user-data-dialog.html',
                    controller: 'UserDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                city: null,
                                country: null,
                                birthday: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-data', null, { reload: true });
                }, function() {
                    $state.go('user-data');
                });
            }]
        })
        .state('user-data.edit', {
            parent: 'user-data',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-data/user-data-dialog.html',
                    controller: 'UserDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserData', function(UserData) {
                            return UserData.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-data', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-data.delete', {
            parent: 'user-data',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-data/user-data-delete-dialog.html',
                    controller: 'UserDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserData', function(UserData) {
                            return UserData.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-data', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
