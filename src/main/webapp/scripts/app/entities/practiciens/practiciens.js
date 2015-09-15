'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('practiciens', {
                parent: 'entity',
                url: '/practicienss',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.practiciens.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/practiciens/practicienss.html',
                        controller: 'PracticiensController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('practiciens');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('practiciens.detail', {
                parent: 'entity',
                url: '/practiciens/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.practiciens.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/practiciens/practiciens-detail.html',
                        controller: 'PracticiensDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('practiciens');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Practiciens', function($stateParams, Practiciens) {
                        return Practiciens.get({id : $stateParams.id});
                    }]
                }
            })
            .state('practiciens.new', {
                parent: 'practiciens',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/practiciens/practiciens-dialog.html',
                        controller: 'PracticiensDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {matricule: null, tel: null, email: null, type: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('practiciens', null, { reload: true });
                    }, function() {
                        $state.go('practiciens');
                    })
                }]
            })
            .state('practiciens.edit', {
                parent: 'practiciens',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/practiciens/practiciens-dialog.html',
                        controller: 'PracticiensDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Practiciens', function(Practiciens) {
                                return Practiciens.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('practiciens', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
