'use strict';

angular.module('cmsApp').controller('InstitutionSanitaireDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'InstitutionSanitaire', 'Hospitalisation',
        function($scope, $stateParams, $modalInstance, entity, InstitutionSanitaire, Hospitalisation) {

        $scope.institutionSanitaire = entity;
        $scope.hospitalisations = Hospitalisation.query();
        $scope.load = function(id) {
            InstitutionSanitaire.get({id : id}, function(result) {
                $scope.institutionSanitaire = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:institutionSanitaireUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.institutionSanitaire.id != null) {
                InstitutionSanitaire.update($scope.institutionSanitaire, onSaveFinished);
            } else {
                InstitutionSanitaire.save($scope.institutionSanitaire, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
