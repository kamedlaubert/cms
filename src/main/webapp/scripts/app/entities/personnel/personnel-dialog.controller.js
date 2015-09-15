'use strict';

angular.module('cmsApp').controller('PersonnelDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Personnel', 'Consultation', 'Conjoint', 'Enfant',
        function($scope, $stateParams, $modalInstance, entity, Personnel, Consultation, Conjoint, Enfant) {

        $scope.personnel = entity;
        $scope.consultations = Consultation.query();
        $scope.conjoints = Conjoint.query();
        $scope.enfants = Enfant.query();
        $scope.load = function(id) {
            Personnel.get({id : id}, function(result) {
                $scope.personnel = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:personnelUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.personnel.id != null) {
                Personnel.update($scope.personnel, onSaveFinished);
            } else {
                Personnel.save($scope.personnel, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
