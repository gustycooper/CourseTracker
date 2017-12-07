//
//  AssignmentCell.swift
//  CourseTracker
//
//  Created by Diego  Bustamante on 12/4/17.
//  Copyright © 2017 Diego Bustamante. All rights reserved.
//

import UIKit

class AssignmentCell: UITableViewCell {
    
    @IBOutlet weak var name: UILabel!
    @IBOutlet weak var due: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}
